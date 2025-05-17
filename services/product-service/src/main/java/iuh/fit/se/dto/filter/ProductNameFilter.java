package iuh.fit.se.dto.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;

public class ProductNameFilter implements ProductFilter {
    private final ProductFilter baseFilter;
    private final String productName;

    public ProductNameFilter(ProductFilter baseFilter, String productName) {
        this.baseFilter = baseFilter;
        this.productName = productName;
    }

    @Override
    public Query applyFilter(Query query) {
        query = baseFilter.applyFilter(query);
        
        if (productName != null && !productName.isEmpty()) {
            // Tạo text index trước (chỉ cần làm 1 lần)
            
            TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matchingAny(productName.split(" ")) // Tách từ khóa thành các từ
                .caseSensitive(false);
            
            query.addCriteria(criteria);
        }
        return query;
    }

}