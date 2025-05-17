package iuh.fit.se.dto.product;

import lombok.Data;

public record SearchCriteria( 
  String productName,
//  String location;
  Double minPrice,
  Double maxPrice,
  Boolean isNew,
  Double minRating,
  String sort
  )
{}