import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.ScooterApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierTest {

    private ScooterApi api;
    private Courier courier;

    @Before
    public void setUp(){
        api = new ScooterApi();
        courier = new Courier("ruslan1239", "qwerty123321", "RuslanRuslan");
    }

    @After
    public void tearDown(){
        Response loginCourierResponse = api.loginCourier(courier.getLogin(), courier.getPassword());
        if (loginCourierResponse.statusCode() == 200){
            int courierId = loginCourierResponse.getBody().jsonPath().getInt("id");
            api.deleteCourier(courierId).then().statusCode(200);
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void createCourierReturnsCorrectStatusAndBodyTest(){
        Response response = api.createCourier(courier);
        response.then().statusCode(201);
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать 2-х одинаковых курьеров")
    public void cannotCreateDuplicateCouriersTest(){
        api.createCourier(courier).then().statusCode(201);
        Response courier2Response = api.createCourier(courier);
        courier2Response.then().statusCode(409);
        courier2Response.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей возвращает ошибку")
    public void cannotCreateCourierWithoutRequiredFieldsTest() {
        Courier courierWithoutLogin = new Courier(null, "qwerty123", "courierWithoutLogin");
        Response responseWithoutLogin = api.createCourier(courierWithoutLogin);
        responseWithoutLogin.then().statusCode(400);
        responseWithoutLogin.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));

        Courier courierWithoutPassword = new Courier("loginchik", null, "courierWithoutPassword");
        Response responseWithoutPassword = api.createCourier(courierWithoutPassword);
        responseWithoutPassword.then().statusCode(400);
        responseWithoutPassword.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание пользователя с логином, который уже есть, возвращает ошибку")
    public void createCourierWithExistingLoginReturnsErrorTest() {
        api.createCourier(courier).then().statusCode(201);
        Courier duplicateCourier = new Courier(courier.getLogin(), "otherPassword", "OtherName");
        Response response = api.createCourier(duplicateCourier);
        response.then().statusCode(409);
        response.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
