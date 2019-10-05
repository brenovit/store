package io.github.brenovit.store.repository;

import io.github.brenovit.store.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String>{

}
