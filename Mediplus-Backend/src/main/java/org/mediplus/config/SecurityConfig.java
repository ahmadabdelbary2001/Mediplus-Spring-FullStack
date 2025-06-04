package org.mediplus.config;

import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CustomUserDetailsService uds,
                          PasswordEncoder encoder,
                          CorsConfigurationSource corsSource) {
        this.userDetailsService = uds;
        this.passwordEncoder      = encoder;
        this.corsConfigurationSource = corsSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patients/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/appointments").permitAll()
                        .requestMatchers("/api/users/me").hasAnyRole("PATIENT","DOCTOR","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients/me").hasAnyRole("PATIENT","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("PATIENT","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/**").hasAnyRole("PATIENT","DOCTOR","ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            log.info("Login successful for user: {}", auth.getName());
                            Cookie sessionCookie = new Cookie("JSESSIONID", req.getSession().getId());
                            sessionCookie.setPath("/");
                            sessionCookie.setHttpOnly(true);
                            sessionCookie.setSecure(true);
                            sessionCookie.setAttribute("SameSite", "None");

                            res.addCookie(sessionCookie);
                            res.setStatus(HttpStatus.OK.value());
                            res.setContentType("text/plain");
                            res.getWriter().write("Login successful");
                        })
                        .failureHandler((req, res, ex) -> {
                            log.error("Login failed: {}", ex.getMessage());
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setContentType("text/plain");
                            res.getWriter().write("Login failed: " + ex.getMessage());
                        })
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authEx) -> {
                            log.warn("Unauthorized access: {}", authEx.getMessage());
                            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                        })
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }
}