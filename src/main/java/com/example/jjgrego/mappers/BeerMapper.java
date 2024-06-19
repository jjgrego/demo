package com.example.jjgrego.mappers;

import com.example.jjgrego.entities.Beer;
import com.example.jjgrego.model.BeerDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);

}
