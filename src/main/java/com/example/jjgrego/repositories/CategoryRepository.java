package com.example.jjgrego.repositories;

import com.example.jjgrego.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, UUID> {

}
