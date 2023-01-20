package com.security.jwt.service;

import com.security.jwt.dto.AuthenticationRequestDto;
import com.security.jwt.dto.AuthenticationResponseDto;
import com.security.jwt.dto.RegisterRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request);
    
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
}
