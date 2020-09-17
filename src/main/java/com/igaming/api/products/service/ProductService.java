package com.igaming.api.products.service;

import com.igaming.api.products.controller.dto.ProductPayload;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.exception.NotFoundProductException;
import com.igaming.api.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<Product> getProducts(int page, int size, @Nullable String sortBy) {
        return repository.findAll(createSort(sortBy))
            .skip(PageRequest.of(page, size).getOffset())
            .take(size);
    }

    public Mono<Product> getProduct(@NotNull Long id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundProductException(id)));
    }

    public Mono<Product> createProduct(@NotNull ProductPayload payload) {
        return repository.save(
            Product.create(payload.getName(), payload.getPrice())
        );
    }

    public Mono<Product> updateProduct(@NotNull Long id,
                                       @NotNull ProductPayload payload) {
        return getProduct(id)
            .map(updateProductFrom(payload))
            .flatMap(repository::save);
    }

    public Mono<Void> removeProduct(@NotNull Long id) {
        return getProduct(id)
            .flatMap(repository::delete);
    }

    private Sort createSort(String sortBy) {
        return sortBy == null ? Sort.unsorted() : Sort.by(sortBy);
    }

    private Function<Product, Product> updateProductFrom(ProductPayload payload) {
        return product -> {
            product.setName(payload.getName());
            product.setPrice(payload.getPrice());
            return product;
        };
    }
}
