package com.example.jjgrego.services;

import com.example.jjgrego.model.BeerDTO;
import com.example.jjgrego.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO addNewBrand(BeerDTO b);

    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO b);

    boolean deleteById(UUID id);

    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO b);
}
