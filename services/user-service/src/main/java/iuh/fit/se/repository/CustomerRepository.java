package iuh.fit.se.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import iuh.fit.se.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
