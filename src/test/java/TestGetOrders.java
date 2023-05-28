import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestGetOrders {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Метод GET/orders. Без параметров")
    public void checkGetOrdersWithoutParamsSuccess() {
        Response response =
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .statusCode(200);
    }

    @Test
    @DisplayName("Метод GET/orders. courierId в параметрах")
    public void checkGetOrdersCourierSelectedSuccess() {
        int courierId = 194993;
        ArrayList<Integer> actualId =
        given()
                .header("Content-type", "application/json")
                //.body("")
                //.param(courierId, 555)
                .when()
                .get("/api/v1/orders?courierId=" + courierId)
                .then()
                .statusCode(200)
                .extract().jsonPath().get("orders.courierId");
        Assert.assertTrue(actualId.contains(courierId));
    }

    @Test
    @DisplayName("Метод GET/orders. Не существующий courierId")
    public void checkGetOrdersOfOneCourierNotFound() {
        int courierId = 5555555;
        given()
                .header("Content-type", "application/json")
                //.body("")
                //.param(courierId, 555)
                .when()
                .get("/api/v1/orders?courierId=" + courierId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
    }

    @Test
    @DisplayName("Метод GET/orders. Указан ID станции метро")
    public void checkGetOrdersStationSelectedSuccess() {
        ArrayList<String> actualStation = new ArrayList<>();
        actualStation.add("Красносельская");
        Response response = given()
                .header("Content-type", "application/json")
                //.body("")
                //.param(courierId, 555)
                .when()
                .get("/api/v1/orders?nearestStation=[\"5\"]");
        response.then().assertThat().body("availableStations.name", equalTo(actualStation))
                .statusCode(200);
    }

    @Test
    @DisplayName("Метод GET/orders. Не существующий ID станции метро")
    public void checkGetOrdersStationNotFound() {
        ArrayList<Integer> availableStations =
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?nearestStation=[\"888\"]")
                .then()
                .statusCode(200)
                .extract().jsonPath().get("availableStations");
        Assert.assertTrue(availableStations.isEmpty());
    }

    @Test
    @DisplayName("Метод GET/orders. Указан лимит заказов")
    public void checkGetOrdersLimitsSelectedSuccess() {
        int limit = 5;
        ArrayList<Integer> orders =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders?limit=" + limit)
                        .then()
                        .statusCode(200)
                        .extract().jsonPath().get("orders");
        Assert.assertEquals(limit, orders.size());
    }

    @Test
    @DisplayName("Метод GET/orders. Лимит заказов по умолчанию")
    public void checkGetOrdersOverLimits30ByDefault() {
        int limit = 35;
        ArrayList<Integer> orders =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders?limit=" + limit)
                        .then()
                        .statusCode(200)
                        .extract().jsonPath().get("orders");
        Assert.assertEquals(30, orders.size());
    }

    @Test
    @DisplayName("Метод GET/orders. Указан номер страницы")
    public void checkGetOrdersPageSuccess() {
        int page = 5;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders?page=" + page);
        response.then().assertThat().body("pageInfo.page", equalTo(page))
                .statusCode(200);
    }
}
