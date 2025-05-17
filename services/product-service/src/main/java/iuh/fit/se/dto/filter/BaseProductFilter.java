package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Query;

public class BaseProductFilter implements ProductFilter {
	@Override
    public Query applyFilter(Query query) {
        return query;
        
    }
}