package iuh.fit.se.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class UserResponseDTO {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private String imgUrl;
}