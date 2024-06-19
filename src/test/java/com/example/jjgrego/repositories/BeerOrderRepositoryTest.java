package com.example.jjgrego.repositories;

import com.example.jjgrego.entities.Beer;
import com.example.jjgrego.entities.BeerOrder;
import com.example.jjgrego.entities.BeerOrderShipment;
import com.example.jjgrego.entities.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderRepositoryTest {
@Autowired
BeerOrderRepository beerOrderRepository;
@Autowired
CustomerRepository customerRepository;

@Autowired
BeerRepository beerRepository;

Customer customer;
Beer beer;

@BeforeEach
void setup(){
    customer = customerRepository.findAll().get(0);
    beer = beerRepository.findAll().get(0);
}
    @Test
    @Transactional
    void testBeerOrders() {

        BeerOrder bo = BeerOrder.builder()
                .customerRef("Test Order")
                .customer(customer)
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("12AC4FE79")
                        .build())
                .build();

        BeerOrder savedBO = beerOrderRepository.save(bo);

        System.out.println(savedBO);
    }
}
