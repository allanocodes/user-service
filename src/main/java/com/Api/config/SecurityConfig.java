package com.Api.config;

import com.Api.filters.JwtFilter;
import com.Api.service.UserDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.filters","com.service"})
public class SecurityConfig {

    @Autowired
    UserDetailImpl userDetail;

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     return   http.authorizeHttpRequests(auth ->
                       auth.requestMatchers("/security/create" ).permitAll()
                               .requestMatchers("/security/userLogin").permitAll()
                               .requestMatchers("/security/findBy/**").permitAll()
                               .requestMatchers("/sendMessage").permitAll()
                               .anyRequest().authenticated()

        ).formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults())
             .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()).build();

    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetail);

        return authenticationProvider;
    }

}
