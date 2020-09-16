package com.igaming.api.products;

import com.igaming.api.products.config.IntegrationTest;
import com.igaming.api.products.domain.Product;
import com.igaming.api.products.utils.Endpoints;
import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class GetProductsIT extends IntegrationTest {

    @Test
    void getAllProducts() {
        // given
        saveProducts(
            Product.create("Milk", BigDecimal.valueOf(1.12)),
            Product.create("Chocolate", BigDecimal.valueOf(2.3)),
            Product.create("Tea", BigDecimal.valueOf(0.7))
        );

        // when
        String responseBody = RestAssured
            .given()
            .get(Endpoints.GET_PRODUCTS)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .getBody()
            .asString();

        // then
        assertThatJson(responseBody)
            .when(Option.IGNORING_ARRAY_ORDER)
            .isEqualTo(
                getClassPathFileContent("/test-data/get-products-it/get-all-products/response.json")
            );
    }

    @Test
    void getTwoSortedProducts() {
        // given
        saveProducts(
            Product.create("Milk", BigDecimal.valueOf(1.12)),
            Product.create("Chocolate", BigDecimal.valueOf(2.3)),
            Product.create("Tea", BigDecimal.valueOf(0.7))
        );

        // when
        String responseBody = RestAssured
            .given()
            .queryParam("page", 0)
            .queryParam("size", 2)
            .queryParam("sortBy", "name")
            .get(Endpoints.GET_PRODUCTS)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .getBody()
            .asString();

        // then
        assertThatJson(responseBody)
            .when(Option.IGNORING_ARRAY_ORDER)
            .isEqualTo(
                getClassPathFileContent("/test-data/get-products-it/get-2-sorted-products/response.json")
            );
    }

    @Test
    void invalidPageQueryParameter() {
        // when
        RestAssured
            .given()
            .queryParam("page", -1)
            .get(Endpoints.GET_PRODUCTS)
            .then()
            .statusCode(400);
    }

    @Test
    void invalidSizeQueryParameter() {
        // when
        RestAssured
            .given()
            .queryParam("size", 0)
            .get(Endpoints.GET_PRODUCTS)
            .then()
            .statusCode(400);
    }

    @Test
    void invalidSortByQueryParameter() {
        // when
        RestAssured
            .given()
            .queryParam("sortBy", "id")
            .get(Endpoints.GET_PRODUCTS)
            .then()
            .statusCode(400);
    }
}
