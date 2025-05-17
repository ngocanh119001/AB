package iuh.fit.se.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration

public class ServiceConfig {
	
	@Value("${aws.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.secretKey}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;
	
	@Bean
    S3AsyncClient s3AsyncClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretKey);
        return S3AsyncClient.builder()
                .region(Region.of(region)) // Sử dụng region từ properties
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
	
	@Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(java.time.LocalDateTime.now());
    }
}
