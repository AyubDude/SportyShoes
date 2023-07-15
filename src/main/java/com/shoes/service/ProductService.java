package com.shoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.model.Product;
import com.shoes.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
    ProductRepository productRepository;

    public void addProduct(Product product){
        productRepository.save(product);
    }
    public Optional<Product> getProductById(int id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void removeProductById(int id){
        productRepository.deleteById(id);
    }

    public List<Product> getAllProductByCategoryId(int id){
        return productRepository.findAllByCategoryId(id);
    }

}
