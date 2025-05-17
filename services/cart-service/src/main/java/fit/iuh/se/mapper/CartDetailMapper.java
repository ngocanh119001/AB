package fit.iuh.se.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import fit.iuh.se.dto.cartDetail.CartDetailCreateRequestDto;
import fit.iuh.se.dto.cartDetail.CartDetailResponse;
import fit.iuh.se.dto.cartDetail.CartDetailUpdateRequestDto;
import fit.iuh.se.model.CartDetail;


@Mapper(componentModel = "spring")
public interface CartDetailMapper {
	@Mapping(target = "cartDetailId", ignore = true) // Ignore the id field in the target entity
	CartDetail fromCartDetailCreateRequestToEntity(CartDetailCreateRequestDto dto);
	
	@Mapping(target = "cartDetailId", ignore = true) // Ignore the id field in the target entity
	CartDetail injectToEntity(@MappingTarget CartDetail cartDetail, CartDetailCreateRequestDto dto);
	
	CartDetailResponse fromEntityToCartDetailResponse(CartDetail entity);
	
	CartDetail fromCartDetailCreateRequestDtoToEntity(CartDetailCreateRequestDto dto);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	CartDetail injectToEntity(@MappingTarget CartDetail cartDetail, CartDetailUpdateRequestDto dto);
	
}
