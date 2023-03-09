package com.security.jwt.service.implementations;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.jwt.config.CustomUserDetails;
import com.security.jwt.config.JwtService;
import com.security.jwt.dto.AuthenticationRequestDto;
import com.security.jwt.dto.AuthenticationResponseDto;
import com.security.jwt.dto.RegisterRequestDto;
import com.security.jwt.dto.RegisterResponseDto;
import com.security.jwt.exceptions.JwtTokenExpiredException;
import com.security.jwt.models.User;
import com.security.jwt.models.Role;
import com.security.jwt.repository.RoleRepository;
import com.security.jwt.repository.UserRepository;
import com.security.jwt.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService{
    
    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {

        Role roles = roleRepository.findByName(request.getRole()).get();

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(roles))
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
        Authentication auth =  authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        if(auth.isAuthenticated()){
            var user = repository.findByEmail(request.getEmail()).map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

            // generate jwt tokens
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("Bad credentials");
        }
        
    }

    @Override
    public AuthenticationResponseDto refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String refreshToken = "";
        String jwtToken = "";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtTokenExpiredException("Invalid JWT signature");
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
                    
                } else {
                    throw new JwtTokenExpiredException("Your token has expired or invalidated. Please login again to generate a new one.");
                }
            }
        } else {
            throw new JwtTokenExpiredException("Invalid JWT signature");
        }
        
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    
}
