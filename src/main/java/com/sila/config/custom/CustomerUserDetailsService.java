package com.sila.config.custom;

import com.sila.config.exception.NotFoundException;
import com.sila.modules.profile.model.User;
import com.sila.modules.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Invalid email");
        }

        return new CustomUserDetails(user); // Return custom UserDetails
    }
}
