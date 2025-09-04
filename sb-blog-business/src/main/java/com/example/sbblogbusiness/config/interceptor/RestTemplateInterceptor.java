package com.example.sbblogbusiness.config.interceptor;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

  private final OAuth2AuthorizedClientManager manager;

  private Logger logger = LoggerFactory.getLogger(RestTemplateInterceptor.class);

  public RestTemplateInterceptor(OAuth2AuthorizedClientManager manager) {
    this.manager = manager;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    String token = null;
    try {
      var attrs = RequestContextHolder.getRequestAttributes();
      if (attrs instanceof ServletRequestAttributes servletAttrs) {
        HttpServletRequest req = servletAttrs.getRequest();
        if (req != null) {
          String header = req.getHeader("Authorization");
          if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
          }
        }
      }

      if (token == null) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth && jwtAuth.getToken() != null) {
          token = jwtAuth.getToken().getTokenValue();
        }
      }

      if (token == null) {
        OAuth2AuthorizedClient authorizedClient =
            manager.authorize(
                OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client")
                    .principal("internal")
                    .build());
        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
          token = authorizedClient.getAccessToken().getTokenValue();
        }
      }

      if (token != null) {
        logger.debug("Rest Template interceptor: attaching Authorization Bearer token");
        request.getHeaders().add("Authorization", "Bearer " + token);
      } else {
        logger.warn("Rest Template interceptor: no token available - proceeding without Authorization header");
      }
    } catch (Exception ex) {
      logger.error(
          "Rest Template interceptor: failed to obtain/forward token - proceeding without Authorization header",
          ex);
    }

    return execution.execute(request, body);
  }
}
