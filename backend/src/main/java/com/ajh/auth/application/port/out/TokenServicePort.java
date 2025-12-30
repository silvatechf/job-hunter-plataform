// backend/src/main/java/com/ajh/auth/application/port/out/TokenServicePort.java
package com.ajh.auth.application.port.out;

import com.ajh.auth.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenServicePort {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}