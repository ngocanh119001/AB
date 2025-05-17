package iuh.fit.se.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoDBQueryUtil {
	
	public static Query cloneQuery(Query originalQuery) {
	    Query clonedQuery = new Query();
	    
	    // Clone criteria (query conditions)
	    originalQuery.getQueryObject().forEach((key, value) -> {
	        clonedQuery.addCriteria(Criteria.where(key).is(value));
	    });
	    
	    // Clone sort (nếu có)
	    if (originalQuery.getSortObject() != null) {
	        clonedQuery.withHint(originalQuery.getSortObject());
	    }
	    
	    // Clone fields projection (nếu có)
	    if (originalQuery.getFieldsObject() != null) {
	        originalQuery.getFieldsObject().forEach((field, inclusion) -> {
	            clonedQuery.fields().include(field);
	        });
	    }
	    
	    // Clone skip và limit (nếu có)
	    if (originalQuery.getSkip() > 0) {
	        clonedQuery.skip(originalQuery.getSkip());
	    }
	    if (originalQuery.getLimit() > 0) {
	        clonedQuery.limit(originalQuery.getLimit());
	    }
	    
	    return clonedQuery;
	}
}

