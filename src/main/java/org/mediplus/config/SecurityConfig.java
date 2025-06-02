package org.mediplus.config;

import org.mediplus.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
public class SecurityConfig {
//
//    private final UserServiceImpl userService;
//
//    public SecurityConfig(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/home", "/auth/**", "/css/**", "/js/**", "/img/**", "/fonts/**", "/mail/**").permitAll()
//                        .requestMatchers("/patient/**").hasRole("PATIENT")
//                        .requestMatchers("/doctor/**").hasRole("DOCTOR")
//                        .requestMatchers("/get-started").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(form -> form
//                        .loginPage("/auth/login")
//                        .successHandler(authenticationSuccessHandler())
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/home")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//            if (authentication.getAuthorities().stream()
//                    .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
//                response.sendRedirect("/doctor/dashboard");
//            } else {
//                response.sendRedirect("/patient/dashboard");
//            }
//        };
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            org.mediplus.model.User user = userService
//                    .findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            User.UserBuilder builder = User.withUsername(user.getUsername())
//                    .password(user.getPassword())
//                    .roles(user.getRole());
//            return builder.build();
//        };
//    }
//
//    @Bean
//    public SpringSecurityDialect springSecurityDialect() {
//        return new SpringSecurityDialect();
//    }

}