package com.blaybus.demo.config;

import com.blaybus.demo.controller.JwtKeyHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class WebConfig {

    @Bean
    Pbkdf2PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String secret) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return new JwtKeyHolder(key);
    }

    @Bean
    JwtDecoder jwtDecoder(JwtKeyHolder keyHolder) {
        return NimbusJwtDecoder.withSecretKey(keyHolder.key()).build();
    }


    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }


    @Bean
    DefaultSecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtDecoder jwtDecoder
    ) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // iframe 허용
            .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/member/signUp").permitAll()
                .requestMatchers("/api/member/issueToken").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
