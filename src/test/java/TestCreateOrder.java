import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CreateOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public TestCreateOrder (String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getVariables() {
        //Тестовые данные
        return new Object[][]{
                {"Naruto", "Uchiha", "Москва, ул. Кленовая, д. 20", 5, "+79998885566", 3, "2023-06-06", "Аккуратнее", new String[]{"BLACK"}},
                {"Naruto", "Uchiha", "Москва, ул. Кленовая, д. 20", 5, "+79998885566", 3, "2023-06-06", "Аккуратнее", new String[]{"GRAY"}},
                {"Naruto", "Uchiha", "Москва, ул. Кленовая, д. 20", 5, "+79998885566", 3, "2023-06-06", "Аккуратнее", new String[]{}},
                {"Naruto", "Uchiha", "Москва, ул. Кленовая, д. 20", 5, "+79998885566", 3, "2023-06-06", "Аккуратнее", new String[]{"BLACK", "GRAY"}},
                {"Naruto", "Uchiha", "Москва, ул. Кленовая, д. 20", 5, "+79998885566", 3, "2023-06-06", "Аккуратнее", new String[]{"BLACK", "GRAY"}},
        };
    }

    @Test
    public void checkCreateOrderBlackSuccess() {
        CreateOrder order = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().statusCode(201)
                        .body("track", notNullValue());
    }
}
