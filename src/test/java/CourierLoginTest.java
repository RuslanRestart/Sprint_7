import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.ScooterApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    private ScooterApi api;
    private Courier courier;

    @Before
    public void setUp(){
        api = new ScooterApi();
        courier = new Courier("ruslan1239", "qwerty123321", "RuslanRuslan");
        api.createCourier(courier).then().statusCode(201);
    }

    @After
    public void tearDown() {
        Response loginCourierResponse = api.loginCourier(courier.getLogin(), courier.getPassword());
        if (loginCourierResponse.statusCode() == 200) {
            int courierId = loginCourierResponse.getBody().jsonPath().getInt("id");
            api.deleteCourier(courierId).then().statusCode(200);
        }
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLoginTest(){
        Response response = api.loginCourier(courier.getLogin(), courier.getPassword());
        response.then().statusCode(200);
        response.then().body("id", notNullValue());
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void loginFailsWithoutRequiredFieldsTest(){
        Response response = api.loginCourier(courier.getLogin(), courier.getPassword());
        response.then().statusCode(200);
        response.then().body("id", notNullValue());

        Response responseWithoutLogin = api.loginCourier(null, "password");
        responseWithoutLogin.then().statusCode(400);
        responseWithoutLogin.then().body("message", equalTo("Недостаточно данных для входа"));

        Response responseWithoutPassword = api.loginCourier("login", null);
        responseWithoutPassword.then().statusCode(400);
        responseWithoutPassword.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Система возвращает ошибку, если указаны неверные логин или пароль")
    public void loginFailsWithIncorrectCredentialsTest() {
        Response wrongLoginResponse = api.loginCourier("wrongCourierLogin", courier.getPassword());
        wrongLoginResponse.then().statusCode(404);
        wrongLoginResponse.then().body("message", equalTo("Учетная запись не найдена"));

        Response wrongPasswordResponse = api.loginCourier(courier.getLogin(), "wrongCourierPassword");
        wrongPasswordResponse.then().statusCode(404);
        wrongPasswordResponse.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    public void loginFailsIfFieldIsMissingTest() {
        Response response = api.loginCourier(null, courier.getPassword());
        response.then().statusCode(400);
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, возвращается ошибка")
    public void loginFailsForNonexistentUserTest() {
        Response response = api.loginCourier("kjfndvknjwbec1n2k", "password");
        response.then().statusCode(404);
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void successfulLoginReturnsIdTest() {
        Response response = api.loginCourier(courier.getLogin(), courier.getPassword());
        response.then().statusCode(200);
        response.then().body("id", notNullValue());
    }
}
