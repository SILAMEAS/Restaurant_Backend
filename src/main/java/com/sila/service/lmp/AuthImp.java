package com.sila.service.lmp;

import com.sila.config.CustomUserDetails;
import com.sila.config.CustomerUserDetailsService;
import com.sila.config.JwtProvider;
import com.sila.dto.request.LoginRequest;
import com.sila.dto.response.AuthResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.User;
import com.sila.repository.UserRepository;
import com.sila.service.AuthService;
import com.sila.service.UserService;
import com.sila.utlis.enums.USER_ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthImp implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final UserService userService;

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new NotFoundException("Invalid email or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    public ResponseEntity<String> register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("Email is already used");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
        newUser.setRole(user.getRole());
        newUser.setAddresses(user.getAddresses());

        if (user.getProfile() != null && !user.getProfile().isEmpty()) {
            newUser.setProfile(user.getProfile());
        }

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);

        log.info("User registered with email: {}", newUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    public ResponseEntity<AuthResponse> login(LoginRequest req) throws Exception {
        Authentication authentication = authenticate(req.getEmail(), req.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication); // Access token
        String refreshToken = jwtProvider.generateRefreshToken(authentication); // Generate refresh token

        log.info("Generated Access Token: {}", jwt);
        log.info("Generated Refresh Token: {}", refreshToken);

        User user = userService.findUserByEmail(req.getEmail());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(refreshToken); // Include refresh token
        response.setUserId(user.getId());
        response.setRole(USER_ROLE.valueOf(role));
        response.setMessage("Login successfully");

        log.info("User logged in: {}", user.getEmail());

        return ResponseEntity.ok(response); // Return response
    }


    public ResponseEntity<AuthResponse> refreshToken(String refreshToken) {
        log.info("Received refresh token: {}", refreshToken);

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            String email = jwtProvider.getEmailFromJwtToken(refreshToken);
            CustomUserDetails userDetails = (CustomUserDetails) customerUserDetailsService.loadUserByUsername(email); // Use the custom UserDetails

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            String newAccessToken = jwtProvider.generateToken(auth);
            String newRefreshToken = jwtProvider.generateRefreshToken(auth); // Generate a new refresh token

            AuthResponse response = new AuthResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken); // Include new refresh token in the response
            response.setUserId(userDetails.getUser().getId()); // Get the user ID from the custom User object
            response.setRole(USER_ROLE.valueOf(userDetails.getAuthorities().iterator().next().getAuthority()));
            response.setMessage("Token refreshed successfully");

            return ResponseEntity.ok(response);
        } else {
            log.warn("Invalid refresh token: {}", refreshToken); // Log invalid token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }




}
