package iuh.fit.se.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRepresentationMapper {
	UserRepresentation toUserRepresentation(iuh.fit.se.model.User user);
}
