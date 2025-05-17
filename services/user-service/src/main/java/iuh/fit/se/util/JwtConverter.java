package iuh.fit.se.util;

import org.springframework.security.oauth2.jwt.Jwt;

public class JwtConverter {
	public static String extractRealmIdFromJwt(Jwt jwt) {
        String issuer = jwt.getClaimAsString("iss");
        if (issuer == null) {
            throw new IllegalStateException("Issuer claim is missing in JWT");
        }
        // Giả sử issuer có dạng http://<host>/realms/<realmId>
        String[] parts = issuer.split("/");
        return parts[parts.length - 1]; // Lấy phần cuối cùng là realmName
    }
	public static String extractUsernameFromJwt(Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        if (username == null) {
            throw new IllegalStateException("Username is missing in JWT");
        }
        // Giả sử issuer có dạng http://<host>/realms/<realmId>
        return username; // Lấy phần cuối cùng là realmName
    }
}	
