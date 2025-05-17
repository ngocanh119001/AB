package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    // Tạo MongoClient, bạn có thể tùy chỉnh địa chỉ kết nối MongoDB tại đây
    @Bean
    public MongoClient mongoClient() {
        // MongoClient URI có thể thay đổi tuỳ theo môi trường
        return MongoClients.create("mongodb://localhost:27017");
    }

    // Cấu hình MongoTemplate để Spring Boot có thể tương tác với MongoDB
    @Bean
    public MongoTemplate mongoTemplate() {
        // Cấu hình MongoTemplate với MongoClient và tên cơ sở dữ liệu
        return new MongoTemplate(mongoClient(), "userServiceDB");
    }
}
