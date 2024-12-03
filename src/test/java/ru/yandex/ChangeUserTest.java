package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
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

public class ChangeUserTest extends StaticData {


    private UserClient userClient;
    private String loginAccessToken;
    private String regAccessToken;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя с авторизацией")
    public void changeUser() {

        User user = randomUser();
        userClient = new UserClient();

        Response response = userClient.create(user);
        regAccessToken = response.as(UserTokens.class).getAccessToken();
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response changedUserResponse = userClient.change(loginAccessToken);
        Response getUserResponse = userClient.get(loginAccessToken);

        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("accessToken", equalTo(regAccessToken));

        loginResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));

        changedUserResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));

        getUserResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));


    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void changeUserWithoutAuthorization() {

        User user = randomUser();
        userClient = new UserClient();

        Response response = userClient.create(user);
        regAccessToken = response.as(UserTokens.class).getAccessToken();
        Response loginResponse = userClient.login(user);
        loginAccessToken = loginResponse.as(UserTokens.class).getAccessToken();
        loginAccessToken = loginAccessToken.replace("Bearer ", "");
        Response changedUserResponse = userClient.changeWithoutAuth();


        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("accessToken", equalTo(regAccessToken));



        changedUserResponse
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));


    }


    @After
    public void tearDown() {
        userClient.delete(loginAccessToken);
    }


}
