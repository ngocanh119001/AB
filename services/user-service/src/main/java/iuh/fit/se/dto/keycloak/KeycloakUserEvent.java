package iuh.fit.se.dto.keycloak;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeycloakUserEvent {
	String eventType;
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String phone;
    String realmId;
    String clientId;
    List<String> realmRoles;
}
