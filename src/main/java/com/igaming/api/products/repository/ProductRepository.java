package com.igaming.api.products.repository;

import com.igaming.api.products.domain.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
}
