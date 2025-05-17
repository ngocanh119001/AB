package iuh.fit.se.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import iuh.fit.se.model.User;

public interface UserRepository extends MongoRepository<User, String>{
	Optional<User> findByUsername(String username);
}
