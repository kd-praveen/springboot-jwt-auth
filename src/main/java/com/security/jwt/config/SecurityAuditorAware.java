package com.security.jwt.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        
        return Optional.ofNullable("praveen@wizroots.com");
    }

    // To get the userName of the logged in user.

    // @Override
    // public Optional<User> getCurrentAuditor() {

	// 	Authentication authentication = SecurityContextHolder.getContext()
	// 			.getAuthentication();

	// 	if (authentication == null || !authentication.isAuthenticated()) {
	// 		return Optional.empty();
	// 	}

	// 	return Optional.ofNullable((User) authentication.getPrincipal());
	// }

    
    
}
