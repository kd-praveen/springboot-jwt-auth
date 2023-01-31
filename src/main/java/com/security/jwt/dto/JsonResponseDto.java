package com.security.jwt.dto;

import lombok.Data;

@Data
public class JsonResponseDto {
    private Integer statusCode;
    private String message;
}
