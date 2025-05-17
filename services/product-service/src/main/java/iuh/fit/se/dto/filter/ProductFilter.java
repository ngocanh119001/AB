package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Query;

public interface ProductFilter {
	Query applyFilter(Query query);
}