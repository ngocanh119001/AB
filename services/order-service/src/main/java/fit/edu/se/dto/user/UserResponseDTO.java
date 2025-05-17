package fit.edu.se.dto.user;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponseDTO {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private String imgUrl;
}