package com.example.jjgrego.repositories;

import com.example.jjgrego.entities.Beer;
import com.example.jjgrego.entities.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

@Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setup(){
        testBeer = beerRepository.findAll().get(0);
    }

    @Test
    @Transactional
    void testDategories(){
        Category savedCat = categoryRepository.save(Category.builder()
                                                       .description("test category")
                                                       .build());

        testBeer.addCategory(savedCat);
        beerRepository.save(testBeer);

        System.out.println(testBeer.getBeerName());
    }
}
