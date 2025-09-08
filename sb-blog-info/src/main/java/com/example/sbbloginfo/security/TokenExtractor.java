package com.example.sbbloginfo.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Utility to extract the incoming bearer token from the current request or security context. */
@Component
public class TokenExtractor {

  /** Return the bearer token (without the "Bearer " prefix) if present, otherwise null. */
  public String extractToken() {
    // Try HTTP request header first
    var attrs = RequestContextHolder.getRequestAttributes();
    if (attrs instanceof ServletRequestAttributes servletAttrs) {
      HttpServletRequest req = servletAttrs.getRequest();
      if (req != null) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
          return header.substring(7);
        }
      }
    }

    // Fallback: try SecurityContext (useful if called outside request thread or header missing)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof JwtAuthenticationToken jwtAuth && jwtAuth.getToken() != null) {
      return jwtAuth.getToken().getTokenValue();
    }

    return null;
  }
}
