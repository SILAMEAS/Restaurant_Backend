package com.sila.config.context;

import com.sila.model.User;
import com.sila.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserContextFilter extends OncePerRequestFilter {

    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");

        if (jwt != null && !jwt.isEmpty()) {
            try {
                User user = userService.getByJwt(jwt);
                UserContext.setUser(user);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                // Optionally log or handle invalid token
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear(); // Always clear to prevent memory leaks
        }
    }
}
