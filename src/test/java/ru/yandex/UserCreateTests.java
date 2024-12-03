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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.creator.UserCreator.*;

public class UserCreateTests extends StaticData {

    private static UserClient userClient;
    private static String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_PAGE;
    }

    @Test
    @DisplayName("Проверка создания рандомного пользователя")
    public void createUser() {

        User user = randomUser();
        userClient = new UserClient();

        Response response = userClient.create(user);
        accessToken = response.as(UserTokens.class).getAccessToken();
        accessToken = accessToken.replace("Bearer ", "");

        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания рандомного пользователя без пароля")
    public void createUserWithoutPassword() {

        User user = randomUserWithoutPassword();
        userClient = new UserClient();

        Response response = userClient.create(user);

        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания дубликата пользователя")
    public void createUserDuplicate() {

        User user = duplicateUser();
        userClient = new UserClient();

        Response response = userClient.create(user);

        response
                .then().log().all()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("User already exists"));
    }


    @AfterClass
    public static void runAfterClass() {
        userClient.delete(accessToken);
    }


}
