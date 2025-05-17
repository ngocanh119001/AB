package iuh.fit.se.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class ProjectSecurityConfig {
	
	private final iuh.fit.se.dto.CorsPropertiesDto corsProperties;
	
	@Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakConverter());
		
		
		ReactiveJwtAuthenticationConverterAdapter reactiveJwtConverter =
		        new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
		
		http
			.cors(corsCfg -> corsCfg.configurationSource(corsConfigurationSource()))
			.addFilterAt((exchange, chain) -> {
		    return exchange.getPrincipal()
		        .doOnNext(principal -> {
		            if (principal instanceof Authentication auth) {
		                log.info("User Authorities: {}", auth.getAuthorities());
		            } else {
		                log.info("No Authentication found in Principal: {}", principal);
		            }
		        })
		        .then(chain.filter(exchange));
		}, SecurityWebFiltersOrder.CORS);

        http
        	.securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Tránh lưu session
            .csrf(csrf -> csrf.disable()) // Tắt CSRF
            .authorizeExchange(authorize -> authorize
                    .pathMatchers(                
                            "/actuator/**", "/api/payments/**","/api/products/search/**", "/api/ocr/**").permitAll() // Mở quyền truy cập Actuator
                    .anyExchange().authenticated()
                );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveJwtConverter)));
        
        return http.build();
    }
	

	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(corsProperties.getAllowedOrigins()); // Cho phép frontend truy cập
        corsConfig.setAllowedMethods(corsProperties.getAllowedMethods());
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
	
}
