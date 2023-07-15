package com.shoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.model.User;
import com.shoes.repository.UserRepository;

@Service
public class UserService {
	
	
	
	@Autowired
	UserRepository userrepository;
	
	public List<User> getAllUsers(){
		return userrepository.findAll();
	}
	
	public Optional<User> getUserById(int id){
		return userrepository.findById(id);
		
	}
	
	public void removeUserById(int id) {
		userrepository.deleteById(id);
	}
	
	public Optional<User> getUserByEmail(String email){
		return userrepository.findUserByEmail(email);
	}

}
