package iuh.fit.se.dto.sort;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

public class CreatedSortStrategy implements SortStrategy {
	
	private boolean ascending;
	
	public CreatedSortStrategy(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public Query applySort(Query query, String field) {
		// TODO Auto-generated method stub
		return query.with(Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, field));
	}
	
    
}