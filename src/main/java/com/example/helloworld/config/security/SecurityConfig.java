package com.example.helloworld.config.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.helloworld.config.ApplicationProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationErrorHandler authenticationErrorHandler;

  private final OAuth2ResourceServerProperties resourceServerProps;

  private final ApplicationProperties applicationProps;

  @Bean
  public WebSecurityCustomizer webSecurity() {
    final var exclusionRegex = "^(?!%s|%s).*$".formatted(
      "/api/protected",
      "/api/admin"
    );

    return web ->
      web.ignoring()
        .regexMatchers(exclusionRegex);
  }

  @Bean
  public SecurityFilterChain httpSecurity(final HttpSecurity http) throws Exception {
    return http.authorizeRequests()
      .antMatchers("/api/protected/*", "/api/admin/*")
        .authenticated()
      .anyRequest()
        .permitAll()
      .and()
        .cors()
      .and()
        .oauth2ResourceServer()
          .authenticationEntryPoint(authenticationErrorHandler)
          .jwt()
            .decoder(makeJwtDecoder())
          .and()
      .and()
        .build();
  }

  private JwtDecoder makeJwtDecoder() {
    final var issuer = resourceServerProps.getJwt().getIssuerUri();
    final var decoder = JwtDecoders.<NimbusJwtDecoder>fromIssuerLocation(issuer);
    final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    final var tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, this::withAudience);

    decoder.setJwtValidator(tokenValidator);
    return decoder;
  }

  private OAuth2TokenValidatorResult withAudience(final Jwt token) {
    final var audienceError = new OAuth2Error(
      OAuth2ErrorCodes.INVALID_TOKEN,
      "The token was not issued for the given audience",
      "https://datatracker.ietf.org/doc/html/rfc6750#section-3.1"
    );

    return token.getAudience().contains(applicationProps.getAudience())
      ? OAuth2TokenValidatorResult.success()
      : OAuth2TokenValidatorResult.failure(audienceError);
  }
}
