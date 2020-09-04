package com.openuniversity.springjwt.models;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openuniversity.springjwt.models.User;
import com.openuniversity.springjwt.security.services.UserDetailsImpl;

public class AuditorAwareImpl implements AuditorAware<String> {
   /*

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Test");
        // Use below commented code when will use Spring Security.
        
    }
*/

	@Override
    public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

    if (authentication == null || !authentication.isAuthenticated()) {
        return null;
    }
   
        if (name != "anonymousUser" ) {
            return Optional.of(name);
        }

        else {

            return Optional.of ("System");

        }

}

}	
	