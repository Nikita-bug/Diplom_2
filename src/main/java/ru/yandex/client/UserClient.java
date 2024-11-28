package ru.yandex.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.models.User;

import java.io.File;

import static io.restassured.RestAssured.given;


public class UserClient {

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/auth/user");
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Изменение пользователя")
    public Response change(String accessToken) {
        File json = new File("src/main/resources/patchUser.json");
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(json)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Изменение пользователя без авторизации")
    public Response changeWithoutAuth() {
        File json = new File("src/main/resources/patchUser.json");
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Запрос пользователя")
    public Response get(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .get("api/auth/user");

    }



}
