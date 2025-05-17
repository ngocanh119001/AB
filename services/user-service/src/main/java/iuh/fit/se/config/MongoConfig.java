package iuh.fit.se.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
    
    @Bean
    MappingMongoConverter mappingMongoConverter(
        MongoDatabaseFactory factory, MongoMappingContext context) {
        
        MappingMongoConverter converter = new MappingMongoConverter(
            new DefaultDbRefResolver(factory), context);
        
        // Cấu hình để sử dụng trường _class thay vì _type
        converter.setTypeMapper(new DefaultMongoTypeMapper(DefaultMongoTypeMapper.DEFAULT_TYPE_KEY));
        return converter;
    }
}