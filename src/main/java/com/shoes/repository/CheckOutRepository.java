package com.shoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoes.model.CheckOut;

public interface CheckOutRepository extends JpaRepository<CheckOut, Integer>  {

}
