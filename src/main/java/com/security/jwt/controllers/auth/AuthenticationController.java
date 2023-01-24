package com.security.jwt.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwt.dto.AuthenticationRequestDto;
import com.security.jwt.dto.AuthenticationResponseDto;
import com.security.jwt.dto.JwtTokenRefreshRequestDto;
import com.security.jwt.dto.RegisterRequestDto;
import com.security.jwt.dto.RegisterResponseDto;
import com.security.jwt.models.User;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    private final UserRepository repository;

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestDto requestDto
    ) {
        if(repository.existsByEmail(requestDto.getEmail())){
            return new ResponseEntity<>("Email already exists !!", HttpStatus.BAD_REQUEST);
        }

        authService.register(requestDto);

        return new ResponseEntity<>("User registered successfully...", HttpStatus.CREATED);
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto requestDto
    ) {
        return ResponseEntity.ok(authService.authenticate(requestDto));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken( HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

}
