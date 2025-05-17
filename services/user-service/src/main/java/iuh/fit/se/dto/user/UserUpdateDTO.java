package iuh.fit.se.dto.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
	private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private MultipartFile userImg;
}