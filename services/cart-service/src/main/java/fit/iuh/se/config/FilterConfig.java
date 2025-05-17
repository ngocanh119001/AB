package fit.iuh.se.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fit.iuh.se.filter.JwtFilter;

@Configuration
public class FilterConfig {
    
    @Bean
    JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}