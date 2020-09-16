package com.igaming.api.products;

import com.igaming.api.products.config.IntegrationTest;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.utils.Endpoints;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class GetProductIT extends IntegrationTest {

    @Test
    void getProductById() {
        // given
        Long id = saveProductsToDB(
            Product.create("Milk", BigDecimal.valueOf(1.12)),
            Product.create("Chocolate", BigDecimal.valueOf(2.3)),
            Product.create("Tea", BigDecimal.valueOf(0.7))
        ).stream()
            .filter(product -> "Milk".equals(product.getName()))
            .map(Product::getId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable find product with name \"Milk\""));

        // when
        String responseBody = RestAssured
            .given()
            .pathParam("id", id)
            .get(Endpoints.GET_PRODUCT)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .getBody()
            .asString();

        // then
        assertThatJson(responseBody)
            .isEqualTo(
                getClassPathFileContent("/test-data/get-product-it/get-product-by-id/response.json")
            );
    }

    @Test
    void notFoundProduct() {
        // when
        RestAssured
            .given()
            .pathParam("id", 1)
            .get(Endpoints.GET_PRODUCT)
            .then()
            .statusCode(404);
    }
}
