package com.epam.esm.config;

import com.epam.esm.exception.SimpleAccessDeniedHandler;
import com.epam.esm.exception.SimpleAuthenticationEntryPoint;
import com.epam.esm.security.jwt.JwtConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Configuration Spring Security
 *
 * @author Dzianis Savastsiuk
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtConfigurer jwtConfigurer;
    private final SimpleAccessDeniedHandler accessDeniedHandler;
    private final SimpleAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer, SimpleAccessDeniedHandler accessDeniedHandler, SimpleAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/rest/certificates/**").permitAll()
                .antMatchers("/rest/users/**").permitAll()
                .antMatchers("/rest/testing").permitAll()
                .anyRequest().authenticated()
                .and().apply(jwtConfigurer);
        return http.build();
    }
        @Bean
        public AuthenticationManager authenticationManagerBean (
                AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }


        @Bean
        protected PasswordEncoder passwordEncoder () {
            return new BCryptPasswordEncoder(12);
        }
    }
