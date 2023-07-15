package com.shoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoes.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
