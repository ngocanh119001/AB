package iuh.fit.se.dto.keycloak;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class KeycloakDeleteUserEvent {
	String eventType;
	String userId;
	String realmId;
}
