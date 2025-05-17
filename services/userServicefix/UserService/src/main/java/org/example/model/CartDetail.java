package org.example.model;

import lombok.Data;

@Data
public class CartDetail {
    private String productId;
    private int quantity;
    private String customerId;
    private String categoryName;
    private String subCategoryName;
}
