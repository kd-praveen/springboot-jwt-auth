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
import com.security.jwt.dto.JsonResponseDto;
import com.security.jwt.dto.RegisterRequestDto;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    private final UserRepository repository;

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<JsonResponseDto> register(
            @RequestBody RegisterRequestDto requestDto
    ) {
        if(repository.existsByEmail(requestDto.getEmail())){
            JsonResponseDto errorResponse = new JsonResponseDto();

            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setMessage("Email already exists !!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        authService.register(requestDto);

        JsonResponseDto successResponse = new JsonResponseDto();

        successResponse.setStatusCode(HttpStatus.CREATED.value());
        successResponse.setMessage("User registered successfully...");
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
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
