import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.SignUp;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestSignUpCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void checkSignUpSuccess() {
        SignUp reg  = new SignUp("ninjaTTTT", "1234", "saske");
        Response response =
        given()
                .header("Content-type", "application/json")
                .body(reg)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(201)
                .body("ok", is(true));
    }

    @Test
    public void checkSignUpDuplicate() {
        SignUp reg  = new SignUp("ninjaTTTT", "1234", "saske");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }


    @Test
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
    public void checkSignUpWithoutPassword() {
        SignUp reg  = new SignUp("bbbbnnn", "", "saske");
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
    public void checkSignUpWithoutName() {
        SignUp reg  = new SignUp("wwwvvv", "dfdfdss", "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(reg)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201)
                .body("ok", is(true));
    }
}
