package com.prgrms.airbnb.config;

import com.prgrms.airbnb.config.jwt.Jwt;
import com.prgrms.airbnb.config.jwt.JwtAuthenticationFilter;
import com.prgrms.airbnb.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.prgrms.airbnb.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.prgrms.airbnb.domain.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final JwtConfigure jwtConfigure;
  private final UserService userService;

  public WebSecurityConfigure(JwtConfigure jwtConfigure, UserService userService) {
    this.jwtConfigure = jwtConfigure;
    this.userService = userService;
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, e) -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Object principal = authentication != null ? authentication.getPrincipal() : null;
      log.warn("{} is denied", principal, e);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("text/plain;charset=UTF-8");
      response.getWriter().write("ACCESS DENIED");
      response.getWriter().flush();
      response.getWriter().close();
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Jwt jwt() {
    return new Jwt(
        jwtConfigure.getIssuer(),
        jwtConfigure.getClientSecret(),
        jwtConfigure.getExpirySeconds()
    );
  }

  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    Jwt jwt = getApplicationContext().getBean(Jwt.class);
    return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
  }

  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
    Jwt jwt = getApplicationContext().getBean(Jwt.class);
    return new OAuth2AuthenticationSuccessHandler(jwt, userService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/swagger-ui/**", "/v2/api-docs/**", "/asset/**", "/script/**", "/style/**").permitAll()
        .antMatchers("/api/v1/guest/**").hasAnyRole("USER")
        .antMatchers("/api/v1/host/**").hasAnyRole("HOST")
        .antMatchers("/api/v1/**").hasAnyRole("USER", "HOST", "ADMIN")
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .disable()
        .csrf()
        .disable()
        .headers()
        .disable()
        .httpBasic()
        .disable()
        .rememberMe()
        .disable()
        .logout()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestRepository(authorizationRequestRepository())
        .and()
        .successHandler(getApplicationContext().getBean(OAuth2AuthenticationSuccessHandler.class))
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
    ;
  }
}