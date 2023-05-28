import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.SignIn;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestSignInCourier {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Метод POST/login. Успешная авторизация")
    public void checkSignInSuccess() {
        SignIn login = new SignIn("ninjaTTTT", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(200)
                .body("id", notNullValue());

    }

    @Test
    @DisplayName("Метод POST/login. Пользователь не зарегистрирован")
    public void checkSignInNotFound() {
        SignIn login = new SignIn("ninjaxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Метод POST/login. Не указан логин при входе")
    public void checkSignInWithoutLoginBadRequest() {
        SignIn login = new SignIn("", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Метод POST/login. Не указан пароль при входе")
    public void checkSignInWithoutPassBadRequest() {
        SignIn login = new SignIn("ninjaTTTT", "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
