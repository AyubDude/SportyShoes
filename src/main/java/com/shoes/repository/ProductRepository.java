package com.shoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoes.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

    List<Product> findAllByCategoryId(int id);
}
