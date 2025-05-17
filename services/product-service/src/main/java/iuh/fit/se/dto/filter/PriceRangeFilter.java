package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class PriceRangeFilter implements ProductFilter {
    private final ProductFilter baseFilter;
    private final Double minPrice;
    private final Double maxPrice;

    public PriceRangeFilter(ProductFilter baseFilter, Double minPrice, Double maxPrice) {
        this.baseFilter = baseFilter;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public Query applyFilter(Query query) {
        query = baseFilter.applyFilter(query);
        query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
        return query;
    }
}