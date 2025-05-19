package org.mediplus.config;

import org.mediplus.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/auth/**", "/css/**", "/js/**", "/img/**", "/fonts/**", "/mail/**").permitAll()
                        .requestMatchers("/patient/**").hasRole("PATIENT")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/patient/dashboard", true)
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/auth/login?logout")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            org.mediplus.model.User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            User.UserBuilder builder = User.withUsername(user.getUsername());
            builder.password(user.getPassword());  // Password should be encoded
            if (user.getRole() != null) {
                builder.roles(user.getRole());
            }
            return builder.build();
        };
    }
}