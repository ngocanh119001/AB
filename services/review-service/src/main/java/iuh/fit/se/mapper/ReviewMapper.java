package iuh.fit.se.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import iuh.fit.se.dto.ReviewCreateRequestDto;
import iuh.fit.se.dto.ReviewResponseDto;
import iuh.fit.se.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
	@Mapping(target = "reviewId", ignore = true) // Ignore the id field in the target entity
	Review fromReviewCreateRequestToEntity(ReviewCreateRequestDto dto);
	ReviewCreateRequestDto fromEntityToReviewCreateRequest(Review entity);
	
	@Mapping(target = "reviewId", ignore = true) // Ignore the id field in the target entity	
	Review fromReviewCreateRequestinjectToEntity(@MappingTarget Review review, ReviewCreateRequestDto dto);
	
	ReviewResponseDto fromEntityToReviewResponse(Review entity);
	
	
}
