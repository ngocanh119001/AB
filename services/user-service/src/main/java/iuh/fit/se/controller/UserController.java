package iuh.fit.se.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.dto.user.UserUpdateDTO;
import iuh.fit.se.service.UserService;
import iuh.fit.se.util.JwtConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

private final UserService userService;
	
	@GetMapping("/{id}")
    public UserResponseDTO getCustomerById(@PathVariable String id) {
        return userService.getUserById(id);
    }
	
	@PutMapping(path ="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserResponseDTO updateUser(@PathVariable String id, 
	        @AuthenticationPrincipal Jwt jwt, 
	        @ModelAttribute UserUpdateDTO userdto) {
	    String realmId = JwtConverter.extractRealmIdFromJwt(jwt);
	    return userService.updateUser(id, realmId, userdto);
	}	
	
//	@DeleteMapping("/{id}")
//	public boolean deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
//		String realmId = JwtConverter.extractRealmIdFromJwt(jwt);
//		return userService.deleteUser(id, realmId);
//	}
	
	@GetMapping
	public ResponseEntity<CustomPage<UserResponseDTO>> getAllUsers(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    
	    Page<UserResponseDTO> pageResult = userService.getAllUsers(page, size);
	    CustomPage<UserResponseDTO> response = new CustomPage<>(pageResult);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/me")
	public UserResponseDTO getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
		String username = JwtConverter.extractUsernameFromJwt(jwt);
		log.info("Current user: {}", username);
		return userService.getUserByUsername(username);
	}
}
