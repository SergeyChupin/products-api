package com.igaming.api.products;

import com.igaming.api.products.config.IntegrationTest;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.utils.Endpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class UpdateProductIT extends IntegrationTest {

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
        String responseBody = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(getClassPathFileContent("/test-data/update-product-it/update-product/request.json"))
            .pathParam("id", id)
            .put(Endpoints.UPDATE_PRODUCT)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .getBody()
            .asString();

        // then
        assertThatJson(responseBody)
            .isEqualTo(
                getClassPathFileContent("/test-data/update-product-it/update-product/response.json")
            );
    }

    @Test
    void notFoundProductToUpdate() {
        // when
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(getClassPathFileContent("/test-data/update-product-it/update-product/request.json"))
            .pathParam("id", 1)
            .put(Endpoints.UPDATE_PRODUCT)
            .then()
            .statusCode(404);
    }

    @Test
    void invalidProductPayloadToUpdate() {
        // when
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Milk\"}")
            .pathParam("id", 1)
            .put(Endpoints.UPDATE_PRODUCT)
            .then()
            .statusCode(400);
    }
}
