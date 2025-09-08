package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder(
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
    // Use Spring Security helper to create a ReactiveJwtDecoder from issuer location (JWKs
    // discovery)
    return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
  }

  @Bean
  public ReactiveClientRegistrationRepository clientRegistrationRepository(
      @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}") String issuerUri,
      @Value("${spring.security.oauth2.client.registration.gateway-client.client-id}")
          String clientId,
      @Value("${spring.security.oauth2.client.registration.gateway-client.client-secret}")
          String clientSecret,
      @Value("${spring.security.oauth2.client.registration.gateway-client.redirect-uri}")
          String redirectUri) {
    // Build a ClientRegistration for the gateway-client using Keycloak OIDC endpoints
    String authUri = issuerUri + "/protocol/openid-connect/auth";
    String tokenUri = issuerUri + "/protocol/openid-connect/token";
    String jwkSetUri = issuerUri + "/protocol/openid-connect/certs";
    String userInfoUri = issuerUri + "/protocol/openid-connect/userinfo";

    ClientRegistration registration =
        ClientRegistration.withRegistrationId("gateway-client")
            .clientId(clientId)
            .clientSecret(clientSecret)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(redirectUri)
            .scope("openid", "profile", "email")
            .authorizationUri(authUri)
            .tokenUri(tokenUri)
            .jwkSetUri(jwkSetUri)
            .userInfoUri(userInfoUri)
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .issuerUri(issuerUri)
            .build();

    return new InMemoryReactiveClientRegistrationRepository(registration);
  }

  @Bean
  public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
    return new WebSessionServerOAuth2AuthorizedClientRepository();
  }

  // Optionally, define a ReactiveUserDetailsService if user management is required locally
  @Bean
  public ReactiveUserDetailsService reactiveUserDetailsService() {
    return username -> null; // Placeholder: Replace with your user lookup logic
  }
}
