package com.sila.config;

import com.sila.config.context.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class IpLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String ip = getClientIp(httpServletRequest);

        final var roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (roles.contains("ROLE_ANONYMOUS")) {
            System.out.println(" =====>  Login As [ROLE_ANONYMOUS]  Request IP: " + ip + " - " + httpServletRequest.getRequestURI());
        } else {
            var user =UserContext.getUser();
            System.out.println(" =====>  Login As ["+user.getRole()+"] Request IP: " + ip + " - " + httpServletRequest.getRequestURI());
        }


        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}