package iuh.fit.se.service;

import org.springframework.data.domain.Page;

import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.dto.user.UserUpdateDTO;

public interface UserService {
	UserResponseDTO getUserById(String userId);
	UserResponseDTO getUserByUsername(String username);
	UserResponseDTO updateUser(String userId, String realmId, UserUpdateDTO userDto);
	boolean deleteUser(String userId, String realmId);
	Page<UserResponseDTO> getAllUsers(int page, int size);
}
