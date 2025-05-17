package iuh.fit.se.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.dto.user.UserUpdateDTO;
import iuh.fit.se.mapper.UserMapper;
import iuh.fit.se.mapper.UserRepresentationMapper;
import iuh.fit.se.model.User;
import iuh.fit.se.repository.UserRepository;
import iuh.fit.se.service.ThirdPartyService;
import iuh.fit.se.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserServiceImpl implements UserService {

	private final Keycloak keycloak;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserRepresentationMapper userRepresentationMapper;
	private final ThirdPartyService thirdPartyService;

	@Override
	public UserResponseDTO getUserById(String userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId).map(user -> userMapper.toUserResponseDTO(user))
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	@Transactional
	public UserResponseDTO updateUser(String userId, String realmId, UserUpdateDTO userDto) {

		Optional<User> existingCustomer = userRepository.findById(userId);
		if (existingCustomer.isPresent()) {
			User user = existingCustomer.get();
			// Cập nhật thông tin khách hàng
			userMapper.updateUser(user, userDto);
			if (userDto.getUserImg() != null) {
				String imgUrl = thirdPartyService.uploadFile(userDto.getUserImg());
				if (user.getImgUrl() != null) {
					thirdPartyService.deleteFile(user.getImgUrl());
				}
				user.setImgUrl(imgUrl);
			}
			UserRepresentation userRepresentation = userRepresentationMapper.toUserRepresentation(user);
			userRepresentation.setId(user.getUserId());
			userRepresentation.setAttributes(Map.of("phone", List.of(user.getPhoneNumber())));

			keycloak.realm(realmId).users().get(user.getUserId()).update(userRepresentation);
			userRepository.save(user);

			return userMapper.toUserResponseDTO(user);
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Override
	public boolean deleteUser(String id, String realmId) {
		Optional<User> existingCustomer = userRepository.findById(id);
		if (existingCustomer.isPresent()) {
			if (existingCustomer.get().getImgUrl() != null) {
				thirdPartyService.deleteFile(existingCustomer.get().getImgUrl());
			}
			keycloak.realm(realmId).users().get(id).remove();
			userRepository.deleteById(id);
		}

		return existingCustomer.isPresent();
	}
	@Override
	public Page<UserResponseDTO> getAllUsers(int page, int size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size);
		Page<User> users = userRepository.findAll(pageable);
		return users.map(user -> userMapper.toUserResponseDTO(user));
	}

	@Override
	public UserResponseDTO getUserByUsername(String username) {
		
		return userRepository.findByUsername(username).map(user -> userMapper.toUserResponseDTO(user))
				.orElseThrow(() -> new RuntimeException("User not found"));
	}
}
