package com.smunity.server.global.security.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthProvider {
    
    Authentication getAuthentication(HttpServletRequest request);
}
