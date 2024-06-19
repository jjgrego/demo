package com.example.jjgrego.repositories;

import com.example.jjgrego.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {


}
