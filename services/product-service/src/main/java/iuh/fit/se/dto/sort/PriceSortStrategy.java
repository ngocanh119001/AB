package iuh.fit.se.dto.sort;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

public class PriceSortStrategy implements SortStrategy {
    private final boolean ascending;

    public PriceSortStrategy(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public Query applySort(Query query, String field) {
        return query.with(Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, field));
    }
}
