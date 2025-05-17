package iuh.fit.se.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "reviews")
@CompoundIndexes({
    @CompoundIndex(name = "product_customer_idx", def = "{'productId': 1, 'customerId': 1}", unique = true)
})
public class Review {
	
	@Id
	String reviewId;
	Integer rating;
	String comment;
	String productId;
	String customerId;
}
