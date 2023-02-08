package com.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.jwt.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByName(String name);

    Optional<Role> findById(Integer id);
}
