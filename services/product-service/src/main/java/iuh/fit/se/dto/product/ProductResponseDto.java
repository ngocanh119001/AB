package iuh.fit.se.dto.product;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductResponseDto {
	String productId;
	String productName;
	Double price;
	Integer stock;
	Integer soldCount;
	String description;
	
	String firstCategoryName;
	String secondCategoryName;
	
	List<String> firstCategories;
	List<String> secondCategories;
	
	String vendorId;
	String brand;
	String coverImage;
	String video;
	boolean isShow;
	boolean isNew;
	List<String> imageList;
	Double ratingAvg;
	
	

	// Add other fields as needed
}
