package com.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    @Builder.Default
    private String tokenType = "Bearer ";

    private String token;

    private String refreshToken;

    public AuthenticationResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }



   
    
}
