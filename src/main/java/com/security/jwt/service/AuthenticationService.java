package com.security.jwt.service;

import org.springframework.http.ResponseEntity;

import com.security.jwt.dto.AuthenticationRequestDto;
import com.security.jwt.dto.AuthenticationResponseDto;
import com.security.jwt.dto.RegisterRequestDto;
import com.security.jwt.dto.RegisterResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    RegisterResponseDto register(RegisterRequestDto request);
    
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    AuthenticationResponseDto refreshToken(HttpServletRequest request);
}
