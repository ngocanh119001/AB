package fit.iuh.se.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Document(collection = "cart-details")
@CompoundIndexes({
    @CompoundIndex(name = "customer_product_unique", def = "{'customer_id': 1, 'product_id': 1}", unique = true)
})
public class CartDetail {
	@Id
	String cartDetailId;
	
	@Field("product_id")
	String productId;
	
    @Indexed // Tạo index để hỗ trợ sắp xếp
    @Field("customer_id")
	String customerId;
    
	Integer quantity;
	@Field("first_category")
	String firstCategory;
	
	@Field("second_category")
	String secondCategory;
	
	@CreatedDate
    @Field("created_at")
	@Indexed
    LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    LocalDateTime updatedAt;
}
