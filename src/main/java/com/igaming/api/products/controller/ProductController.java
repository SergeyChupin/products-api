package com.igaming.api.products.controller;

import com.igaming.api.products.controller.dto.ProductPayload;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public Flux<Product> getProducts(
        @RequestParam(defaultValue = "0") @PositiveOrZero int page,
        @RequestParam(defaultValue = "10") @Positive int size,
        @RequestParam(required = false) @Pattern(regexp = "^(name|createdAt)$") String sortBy
    ) {
        return service.getProducts(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody @Valid ProductPayload payload) {
        return service.saveProduct(payload);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable Long id,
                                       @RequestBody @Valid ProductPayload payload) {
        return service.updateProduct(id, payload);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> removeProduct(@PathVariable Long id) {
        return service.removeProduct(id);
    }
}
