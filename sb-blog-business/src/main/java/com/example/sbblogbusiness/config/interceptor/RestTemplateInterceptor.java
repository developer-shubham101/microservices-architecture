package com.example.sbblogbusiness.config.interceptor;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

  private final OAuth2AuthorizedClientManager manager;

  private Logger logger = LoggerFactory.getLogger(RestTemplateInterceptor.class);

  public RestTemplateInterceptor(OAuth2AuthorizedClientManager manager) {
    this.manager = manager;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    try {
      OAuth2AuthorizedClient authorizedClient =
          manager.authorize(
              OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client")
                  .principal("internal")
                  .build());

      if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
        logger.warn(
            "Rest Template interceptor: no access token available for client 'my-internal-client' - skipping Authorization header");
        return execution.execute(request, body);
      }

      String token = authorizedClient.getAccessToken().getTokenValue();
      logger.debug("Rest Template interceptor: Token : {}", token);
      request.getHeaders().add("Authorization", "Bearer " + token);
    } catch (Exception ex) {
      logger.error("Rest Template interceptor: failed to obtain access token - proceeding without Authorization header", ex);
    }

    return execution.execute(request, body);
  }
}
