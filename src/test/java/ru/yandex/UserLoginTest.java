package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.client.UserClient;
import ru.yandex.data.StaticData;
import ru.yandex.models.User;
import ru.yandex.models.UserTokens;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.creator.UserCreator.randomUser;
import static ru.yandex.creator.UserCreator.wrongUser;

public class UserLoginTest extends StaticData {

    private static UserClient userClient;
    private static String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }


    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUser() {

        User user = randomUser();
        userClient = new UserClient();
        Response response = userClient.create(user);
        accessToken = response.as(UserTokens.class).getAccessToken();
        Response loginResponse = userClient.login(user);

        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));

        loginResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неправильным паролем")
    public void loginWithWrongPassword() {

        User user = wrongUser();
        userClient = new UserClient();
        Response loginResponse = userClient.login(user);

        loginResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }


    @AfterClass
    public static void runAfterClass() {
        userClient.delete(accessToken);
    }

}
