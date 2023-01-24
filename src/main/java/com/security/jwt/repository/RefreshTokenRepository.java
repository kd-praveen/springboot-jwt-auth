package com.security.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.security.jwt.models.RefreshToken;
import com.security.jwt.models.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    int deleteByUser(User user);
    
}
