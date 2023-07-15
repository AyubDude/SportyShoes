package com.shoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.model.CheckOut;
import com.shoes.repository.CheckOutRepository;

@Service
public class CheckOutService {
	
	
	@Autowired
	CheckOutRepository checkOutRepository;
	
	public void saveOrder(CheckOut checkout) {
		checkOutRepository.save(checkout);
	}
	
	public Optional<CheckOut> getOrderById(int id){
		return checkOutRepository.findById(id);
	}
	
	public List<CheckOut> getAllOrders(){
		return checkOutRepository.findAll();
	}
}
