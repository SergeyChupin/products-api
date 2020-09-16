package com.igaming.api.products;

import com.igaming.api.products.config.IntegrationTest;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.utils.Endpoints;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveProductIT extends IntegrationTest {

    @Test
    void updateProduct() {
        // given
        Long id = saveProductsToDB(
            Product.create("Milk", BigDecimal.valueOf(1.12))
        ).stream()
            .filter(product -> "Milk".equals(product.getName()))
            .map(Product::getId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable find product with name \"Milk\""));

        // when
        RestAssured
            .given()
            .pathParam("id", id)
            .delete(Endpoints.REMOVE_PRODUCT)
            .then()
            .statusCode(200);

        // then
        assertThat(getProductsFromDB().stream().count()).isEqualTo(0);
    }

    @Test
    void notFoundProductToRemove() {
        // when
        RestAssured
            .given()
            .pathParam("id", 1)
            .delete(Endpoints.REMOVE_PRODUCT)
            .then()
            .statusCode(404);
    }
}
