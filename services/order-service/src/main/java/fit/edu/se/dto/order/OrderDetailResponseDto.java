package fit.edu.se.dto.order;

@lombok.AllArgsConstructor
@lombok.Data
@lombok.NoArgsConstructor
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailResponseDto {
	String productId;
	Integer quantity;
	Double price;
	String productName;
	String productImage;
	String firstCategory;
	String secondCategory;
}	
