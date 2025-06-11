package com.example.E_commerce.Project.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests((authorize) -> authorize
                        .antMatchers("/verification/**","/register/**", "/**","/forgetPassword/**").permitAll()
                        .antMatchers("/admin").hasRole("ADMIN") // Only ADMIN can access /admin
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // URL to process the login
                        .successHandler(customSuccessHandler()) // Use a custom success handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                )
                .userDetailsService(userDetailsService); // Your custom user details service

        return http.build();
    }

    // Custom success handler to redirect based on user roles
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                // Get the user's roles
                String role = authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .findFirst()
                        .orElse("");

                // Redirect based on role
                if (role.equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin"); // Redirect to admin page
                } else if (role.equals("ROLE_USER")) {
                    response.sendRedirect("/home"); // Redirect to user home page
                } else {
                    response.sendRedirect("/index"); // Fallback redirect
                }
            }
        };
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}