package iuh.fit.se.dto.sort;

import org.springframework.data.mongodb.core.query.Query;

public interface SortStrategy {
	Query applySort(Query query, String filed);
}