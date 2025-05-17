package iuh.fit.se.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import lombok.Data;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsPropertiesDto {
	private List<String> allowedOrigins;
	private List<String> allowedMethods;
}
