package com.serwertetowy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.serwertetowy.config.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                    .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/swagger-ui/index.html",
                        "/api/v1/episode",
                        "/api/v1/episode/**",
                        "/api/v1/episode/upload"
                            )
                    .permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/tags",
                        "/api/v1/tags/**",
                        "/api/v1/series",
                        "/api/v1/series/**",
                        "/api/v1/ratings/series/**",
                        "/api/v1/episode/stream/**"
                        )
                    .permitAll()
                .requestMatchers(HttpMethod.GET,
//                        "/api/v1/user/**/image",
                        "/api/v1/watchflag",
                        "/api/v1/watchflag/**",
                        "/api/v1/watchlist/**"
                )
                    .hasAnyAuthority(ADMIN.name(), MANAGER.name(),USER.name())
                .requestMatchers(HttpMethod.GET,"/api/v1/user/all")
                    .hasAnyAuthority(ADMIN.name(), MANAGER.name())
//                .requestMatchers(HttpMethod.GET, "/api/v1/user/**/image")
//                    .hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(),USER.name())
                .requestMatchers(HttpMethod.POST,
                        "/api/v1/rating/**")
                .hasAnyAuthority(ADMIN.name(), MANAGER.name(),USER.name())
                .requestMatchers(HttpMethod.POST,
                        "api/v1/series",
                        "/api/v1/tags/",
                        "/api/v1/watchflag"
                        )
                    .hasAnyAuthority(ADMIN.name(), MANAGER.name())
                .requestMatchers(HttpMethod.PUT,
                        "/api/v1/episode/**",
                        "/api/v1/rating/**",
                        "/api/v1/tag/**"
                        )
                    .hasAnyAuthority(ADMIN.name(), MANAGER.name())
//                .requestMatchers(HttpMethod.PUT,
//                        "/api/v1/user/**/image").hasAnyAuthority(ADMIN_UPDATE.name(), USER.name())
                .requestMatchers(HttpMethod.PUT,
                        "/api/v1/user/**").hasAnyAuthority(USER.name())
                .anyRequest()
                    .authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
