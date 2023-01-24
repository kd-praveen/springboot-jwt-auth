package com.security.jwt.service.implementations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.TokenMgrError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.security.jwt.config.JwtService;
import com.security.jwt.dto.AuthenticationRequestDto;
import com.security.jwt.dto.AuthenticationResponseDto;
import com.security.jwt.dto.RegisterRequestDto;
import com.security.jwt.dto.RegisterResponseDto;
import com.security.jwt.models.User;
import com.security.jwt.models.Role;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.service.AuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService{
    
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        return RegisterResponseDto
                .builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        
        // validate user credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found", null));

        // generate jwt tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var expirationTime = jwtService.extractExpireTime(jwtToken);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .expires_in(expirationTime)
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String refreshToken = "";
        String jwtToken = "";
        Date expirationTime = new Date();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid JWT signature");
        }

        refreshToken = authHeader.substring(7);

        final Object tokenType = jwtService.extractTokenType(refreshToken);

        if(tokenType.equals("refresh")){
            String userEmail = jwtService.extractUsername(refreshToken);

            if (userEmail != null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
    
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                // generate jwt tokens
                jwtToken = jwtService.generateToken(userDetails);
                expirationTime = jwtService.extractExpireTime(jwtToken);
                    
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid JWT signature");
        }
        
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .expires_in(expirationTime)
                .build();
    }
    
}
