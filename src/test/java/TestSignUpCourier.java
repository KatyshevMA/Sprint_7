import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.SignIn;
import org.example.SignUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestSignUpCourier {
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Метод POST/courier. Успешная регистрация")
    public void checkSignUpSuccess() {
        SignUp reg  = new SignUp("logwww1", "1234", "saske");
        Response response =
        given()
                .header("Content-type", "application/json")
                .body(reg)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(201)
                .body("ok", is(true));

        SignIn login1 = new SignIn("logwww1", "1234");
        id = given()
                .header("Content-type", "application/json")
                .body(login1)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().get("id");
    }

    @Test
    @DisplayName("Метод POST/courier. Повторная регистрация")
    public void checkSignUpDuplicate() {
        SignUp reg  = new SignUp("logwww2", "1234", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .body(reg)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        SignIn login2 = new SignIn("logwww2", "1234");
        id = given()
                .header("Content-type", "application/json")
                .body(login2)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().get("id");
    }

    @Test
    @DisplayName("Метод POST/courier. Не указан логин при регистрации")
    public void checkSignUpWithoutLogin() {
        SignUp reg  = new SignUp("", "1234", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Метод POST/courier. Не указан пароль при регистрации")
    public void checkSignUpWithoutPassword() {
        SignUp reg  = new SignUp("logwww3", "", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Метод POST/courier. Не указано имя при регистрации")
    public void checkSignUpWithoutName() {
        SignUp reg  = new SignUp("logwww4", "dfdfdss", "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201)
                .body("ok", is(true));

        SignIn login3 = new SignIn("logwww4", "dfdfdss");
        id = given()
                .header("Content-type", "application/json")
                .body(login3)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().get("id");
    }

    @After
    public void tearDown() {
        given()
                .header("Content-type", "application/json")
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}");
    }
}
