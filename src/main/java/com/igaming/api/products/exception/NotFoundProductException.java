package com.igaming.api.products.exception;

public class NotFoundProductException extends RuntimeException {

    public NotFoundProductException(Long id) {
        super("Not found product by id [" + id + "]");
    }
}
