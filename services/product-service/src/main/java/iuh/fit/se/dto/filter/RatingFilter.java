package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class RatingFilter implements ProductFilter {
    private final ProductFilter baseFilter;
    private final Double minRating;

    public RatingFilter(ProductFilter baseFilter, Double minRating) {
        this.baseFilter = baseFilter;
        this.minRating = minRating;
    }

	@Override
	public Query applyFilter(Query query) {
		// TODO Auto-generated method stub
		query = baseFilter.applyFilter(query);
		query.addCriteria(Criteria.where("ratingAvg").gte(minRating));
		
		return query;
	}

}