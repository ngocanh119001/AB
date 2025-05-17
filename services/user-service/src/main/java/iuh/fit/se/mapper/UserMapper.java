package iuh.fit.se.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
	UserResponseDTO toUserResponseDTO(iuh.fit.se.model.User user);
	
	User updateUser(@MappingTarget iuh.fit.se.model.User user, iuh.fit.se.dto.user.UserUpdateDTO dto);
	
	
}	
