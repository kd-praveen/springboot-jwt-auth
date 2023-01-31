package com.security.jwt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwt.dto.JsonResponseDto;

@RestController
@RequestMapping("/api/v1/")
public class DemoController {
    
    @GetMapping("user")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<JsonResponseDto> sayHello() {

        JsonResponseDto successResponse = new JsonResponseDto();

        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setMessage("Hello from secured endpoint for USER role");

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<JsonResponseDto> sayHelloAdmin() {
        JsonResponseDto successResponse = new JsonResponseDto();

        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setMessage("Hello from secured endpoint for ADMIN role");

        return ResponseEntity.ok(successResponse);
    }
}
