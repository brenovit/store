package io.github.brenovit.store.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.brenovit.store.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}
