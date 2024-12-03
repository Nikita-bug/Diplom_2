package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.client.OrderClient;
import ru.yandex.client.UserClient;
import ru.yandex.data.StaticData;
import ru.yandex.models.User;
import ru.yandex.models.UserTokens;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static ru.yandex.creator.UserCreator.randomUser;


public class AuthOrdersTest extends StaticData {

    private UserClient userClient;
    private OrderClient orderClient;
    private String loginAccessToken;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }

    @Test
    @DisplayName("Проверка создания заказа")
    public void orderCreate() {


        User user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();


        userClient.create(user);
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response createOrderResponse = orderClient.createOrder(loginAccessToken);

        loginResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));


        createOrderResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("order.owner", notNullValue());


    }

    @Test
    @DisplayName("Проверка создания заказа с неправильным хэшем ингридиентов")
    public void orderCreateWithWrongIds() {


        User user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();


        userClient.create(user);
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response createOrderResponse = orderClient.createOrderWithWrongIds(loginAccessToken);

        createOrderResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .and()
                .assertThat()
                .body(containsString("Internal Server Error"));
    }

    @Test
    @DisplayName("Проверка создания заказа с пустым хэшем ингридиентов")
    public void orderCreateWithEmptyIds() {


        User user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();


        userClient.create(user);
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response createOrderResponse = orderClient.createOrderWithEmptyIds(loginAccessToken);

        createOrderResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @After
    public void tearDown() {
        userClient.delete(loginAccessToken);
    }
}
