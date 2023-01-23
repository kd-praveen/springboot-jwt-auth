package com.security.jwt.dto;

import java.util.Date;

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

    private Date expires_in;

    public AuthenticationResponseDto(String token, String refreshToken, Date expires_in) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expires_in = expires_in;
    }



   
    
}
