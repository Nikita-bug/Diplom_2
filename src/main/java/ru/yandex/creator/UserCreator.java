package ru.yandex.creator;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import ru.yandex.models.User;

public class UserCreator {

    static Faker faker = new Faker();
    static String email = faker.internet().emailAddress();
    static String password = faker.internet().password();
    static String name = faker.name().username();
    static String ST_EMAIL = "duplicate@dub.ru";
    static String ST_PASSWORD = "pass3232";
    static String ST_NAME = "duplicate";

    @Step("Создание рандомного пользователя")
    public static User randomUser() {
        return new User()
                .withEmail(email)
                .withPassword(password)
                .withName(name);
    }
    @Step("Создание рандомного пользователя без пароля")
    public static User randomUserWithoutPassword() {
        return new User()
                .withEmail(email)
                .withName(name);
    }
    @Step("Создание дубликата пользователя")
    public static User duplicateUser() {
        return new User()
                .withEmail(ST_EMAIL)
                .withPassword(ST_PASSWORD)
                .withName(ST_NAME);
    }
    @Step("Создание пользователя для некорректного логина")
    public static User wrongUser() {
        return new User()
                .withEmail(ST_EMAIL)
                .withPassword(ST_PASSWORD+1)
                .withName(ST_NAME);
    }

}
