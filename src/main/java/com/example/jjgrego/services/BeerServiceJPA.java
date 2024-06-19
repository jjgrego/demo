package com.example.jjgrego.services;

import com.example.jjgrego.entities.Beer;
import com.example.jjgrego.mappers.BeerMapper;
import com.example.jjgrego.model.BeerDTO;
import com.example.jjgrego.model.BeerStyle;
import com.example.jjgrego.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
private final BeerRepository beerRepository;
private final BeerMapper beerMapper;

private static final Integer DEFAULT_PAGE_NUMBER = 0;
private static final Integer DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory,
                                   Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;

        if(StringUtils.hasText(beerName) && beerStyle == null){
            int len = beerName.length();

            beerPage =  listBeersByName(beerName, pageRequest);
        } else if(!StringUtils.hasText(beerName) && beerStyle != null){
            beerPage =  listBeersByStyle(beerStyle, pageRequest);
        } else if(StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(!showInventory){
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPgNumber;
        int queryPgSize;
        if (pageNumber != null && pageNumber > 0) {
            queryPgNumber = (pageNumber - 1);
        } else queryPgNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize == null) {
            queryPgSize = DEFAULT_PAGE_SIZE;
        } else queryPgSize = pageSize;
        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPgNumber, queryPgSize, sort);
    }
   public  Page<Beer> listBeersByName (String beerName, PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
    }

    public  Page<Beer> listBeersByStyle (BeerStyle beerStyle, PageRequest pageRequest)  {
        return beerRepository.findAllByBeerStyle(beerStyle,  pageRequest);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest)  {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO addNewBrand(BeerDTO b) {
        Beer beer = beerRepository.save(beerMapper.beerDtoToBeer(b));
        log.info("have saved beer:  {}", beer.toString());
        BeerDTO returning =  beerMapper.beerToBeerDto(beer);
        log.info("returning saved beerDTO:  {}", returning.toString());
        return returning;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO b) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(b.getBeerName());
            foundBeer.setBeerStyle(b.getBeerStyle());
            foundBeer.setUpc(b.getUpc());
            foundBeer.setPrice(b.getPrice());
            foundBeer.setVersion(b.getVersion());
            foundBeer.setQuantityOnHand(b.getQuantityOnHand());
            foundBeer.setUpdatedDate(LocalDateTime.now());
            foundBeer.setCreatedDate(b.getCreatedDate());
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if(beerRepository.existsById(id)){
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                int length = beer.getBeerName().length();
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();


    }
}
