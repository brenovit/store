package io.github.brenovit.store.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.brenovit.store.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
