package iuh.fit.se.dto.sort;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

public class BestSellingSortStrategy implements SortStrategy {

	@Override
	public Query applySort(Query query, String field) {
		// TODO Auto-generated method stub
		return query.with(Sort.by(Sort.Order.desc(field)));
	}
    
}