package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import iuh.fit.se.model.Product;
import reactor.core.publisher.Flux;

public class ProductNewFilter implements ProductFilter {
    private final ProductFilter baseFilter;
    private final boolean condition;

    public ProductNewFilter(ProductFilter baseFilter, boolean condition) {
        this.baseFilter = baseFilter;
        this.condition = condition;
    }

	@Override
	public Query applyFilter(Query query) {
		// TODO Auto-generated method stub
		query = baseFilter.applyFilter(query);
        query.addCriteria(Criteria.where("isNew").is(condition));
        return query;
	}


}
