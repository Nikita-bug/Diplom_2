package ru.yandex.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderClient {


    @Step("Получение заказов пользователя")
    public Response getOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .get("api/orders");

    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("api/orders");

    }

    @Step("Создание заказа")
    public Response createOrder(String accessToken) {
        File json = new File("src/main/resources/ingredient.json");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(json)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа с неправильным хэшем ингридиентов")
    public Response createOrderWithWrongIds(String accessToken) {
        File json = new File("src/main/resources/wrongIdsIngredients.json");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(json)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа с пустым хэшем ингридиентов")
    public Response createOrderWithEmptyIds(String accessToken) {
        File json = new File("src/main/resources/emptyIngredients.json");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(json)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth() {
        File json = new File("src/main/resources/ingredient.json");
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/orders");
    }


}
