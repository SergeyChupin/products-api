package com.igaming.api.products;

import com.igaming.api.products.config.IntegrationTest;
import com.igaming.api.products.utils.Endpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.Matchers.equalTo;

public class CreateProductIT extends IntegrationTest {

    @Test
    void createProduct() {
        // when
        String responseBody = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(getClassPathFileContent("/test-data/create-product-it/create-product/request.json"))
            .post(Endpoints.CREATE_PRODUCT)
            .then()
            .statusCode(201)
            .assertThat()
            .header("Location", response -> equalTo("/api/v1/products/" + response.path("id")))
            .extract()
            .response()
            .getBody()
            .asString();

        // then
        assertThatJson(responseBody)
            .isEqualTo(
                getClassPathFileContent("/test-data/create-product-it/create-product/response.json")
            );
    }

    @Test
    void createTwoProductsWithSameName() {
        // when
        IntStream.range(0, 2).forEach(__ ->
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(getClassPathFileContent("/test-data/create-product-it/create-product/request.json"))
                .post(Endpoints.CREATE_PRODUCT)
                .then()
                .statusCode(201)
                .assertThat()
                .header("Location", response -> equalTo("/api/v1/products/" + response.path("id")))
        );

        // then
        Assertions.assertThat(
            getProductsFromDB().stream()
                .filter(product -> "Milk".equals(product.getName()))
                .count()
        ).isEqualTo(2);
    }

    @Test
    void invalidProductPayloadToCreate() {
        // when
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Milk\"}")
            .post(Endpoints.CREATE_PRODUCT)
            .then()
            .statusCode(400);
    }
}
