package com.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.security.jwt.models.User;

public interface UserRepository extends JpaRepository<User, Integer>, RevisionRepository<User, Integer, Integer> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}
