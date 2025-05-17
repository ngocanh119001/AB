package iuh.fit.se.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import iuh.fit.se.model.Vendor;

public interface VendorRepository extends MongoRepository<Vendor, String> {
}
