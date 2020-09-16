package com.igaming.api.products.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Table("products")
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private Instant createdAt;

    public static Product create(@NonNull String name,
                                 @NonNull BigDecimal price) {
        return new Product(null, name, price, Instant.now());
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must not be negative or zero");
        }
        this.price = price;
    }
}
