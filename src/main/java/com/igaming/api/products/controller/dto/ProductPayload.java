package com.igaming.api.products.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class ProductPayload {
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private BigDecimal price;
}
