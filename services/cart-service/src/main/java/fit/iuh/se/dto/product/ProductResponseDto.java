package fit.iuh.se.dto.product;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
	private String productId;
	private String productName;
	private Double price;
	private Integer stock;
	private Integer soldCount;
	private String description;
	private String vendorId;
	private String brand;
	private String coverImage;
	private String video;
	private boolean isShow;
	private boolean isNew;
	private List<String> imageList;
	private Double ratingAvg;
	// Add other fields as needed
}
