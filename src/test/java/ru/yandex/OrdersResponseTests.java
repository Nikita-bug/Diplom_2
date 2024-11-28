package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.client.OrderClient;
import ru.yandex.client.UserClient;
import ru.yandex.data.StaticData;
import ru.yandex.models.User;
import ru.yandex.models.UserTokens;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.creator.UserCreator.randomUser;

public class OrdersResponseTests extends StaticData {


    private static UserClient userClient;
    private OrderClient orderClient;
    private static String loginAccessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }

    @Test
    @DisplayName("Проверка получения списка заказов пользователя")
    public void getUserOrders() {

        User user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();

        userClient.create(user);
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response getOrdersResponse = orderClient.getOrders(loginAccessToken);


        getOrdersResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка получения списка заказов пользователя без авторизации")
    public void getUserOrdersWithoutAuth() {

        User user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();

        userClient.create(user);
        Response getOrdersResponse = orderClient.getOrdersWithoutAuth();


        getOrdersResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }


    @AfterClass
    public static void runAfterClass() {
        userClient.delete(loginAccessToken);
    }


}
