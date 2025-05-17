package iuh.fit.se.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    Keycloak keycloak(
            @Value("${keycloak.admin-url}") String serverUrl,
            @Value("${keycloak.admin-username}") String username,
            @Value("${keycloak.admin-password}") String password,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client-id}") String clientId) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm) // Realm master cho admin
                .username(username)
                .password(password)
                .clientId(clientId) // Client ID cho Admin REST API
                .build();
    }
}