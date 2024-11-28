package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.client.OrderClient;
import ru.yandex.data.StaticData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class NotAuthOrdersTests extends StaticData {


    private OrderClient orderClient;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }


    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void orderCreateWithoutAuth() {

        orderClient = new OrderClient();
        Response createOrderResponse = orderClient.createOrderWithoutAuth();

        createOrderResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("order", notNullValue());

    }


}





