package iuh.fit.se.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@Document(collection = "products")
public class Product implements Serializable{
	
	private static final long serialVersionUID = -52539057470126613L;
	
	@Id
	String productId;
	@Indexed
	String productName;
	@Indexed
	Double price;
	Integer stock;
	@Indexed
	Integer soldCount;
	String description;
	
	String firstCategoryName;
	String secondCategoryName;
	
	List<String> firstCategories;
	List<String> secondCategories;
	
	String vendorId;
	Images images;
	Double ratingAvg;
	String brand;
	@Builder.Default
	boolean isShow = true;
	@Builder.Default
	boolean isNew = true;
	
	
	@CreatedDate
    @Field("created_at")
	@Indexed
    LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    LocalDateTime updatedAt;

	public void addImage(String image) {
		if (images == null) {
			images = new Images();
		}
		images.addImage(image);
	}
	public void removeImage(String image) {
		if (images != null) {
			images.removeImage(image);
		}
	}
	public void addVideo(String video) {
		if (images == null) {
			images = new Images();
		}
		images.setVideo(video);
	}
	public void removeVideo() {
		if (images != null) {
			images.setVideo(null);
		}
	}
	public void addCoverImage(String coverImage) {
		if (images == null) {
			images = new Images();
		}
		images.setCoverImage(coverImage);
	}
	public void removeCoverImage() {
		if (images != null) {
			images.setCoverImage(null);
		}
	}
	public void increaseStock(int quantity) {
		this.stock += quantity;
	}
	public void decreaseStock(int quantity) {
		this.stock -= quantity;
		if (this.stock < 0) {
			this.stock = 0;
		}
	}
}

