package iuh.fit.se.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Document(collection = "users")
@NoArgsConstructor
@SuperBuilder
public abstract class User {
	@Id
	private String userId;
	@Indexed(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String clientId;
    private List<UserRole> roles;
    private String imgUrl;
    
    
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public void addRole(UserRole role) {
		if (this.roles == null) {
			this.roles = new java.util.ArrayList<>();
		}
		this.roles.add(role);
	}
    
}
