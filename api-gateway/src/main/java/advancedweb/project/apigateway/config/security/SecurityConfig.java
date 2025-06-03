package advancedweb.project.apigateway.config.security;

import advancedweb.project.apigateway.config.security.jwt.TokenProvider;
import advancedweb.project.apigateway.config.security.path.ExcludeAuthPathProperties;
import advancedweb.project.apigateway.filter.JwtAuthenticationFilter;
import advancedweb.project.apigateway.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CorsProperties corsProperties;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(auth -> {
                    auth
                            .pathMatchers(HttpMethod.OPTIONS).permitAll();

                    excludeAuthPathProperties.getPaths().forEach(authPath -> {
                        HttpMethod method = HttpMethod.valueOf(authPath.getMethod());
                        if (method != null) {
                            auth.pathMatchers(method, authPath.getPathPattern())
                                    .permitAll();
                        }
                    });

                    auth.anyExchange().permitAll();
                })
                .addFilterAt(new JwtAuthenticationFilter(tokenProvider, excludeAuthPathProperties), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOriginPatterns(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
