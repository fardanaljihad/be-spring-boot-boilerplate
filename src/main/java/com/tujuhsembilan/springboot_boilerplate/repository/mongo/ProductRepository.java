package com.tujuhsembilan.springboot_boilerplate.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tujuhsembilan.springboot_boilerplate.model.document.Product;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceGreaterThan(BigDecimal price);
}
