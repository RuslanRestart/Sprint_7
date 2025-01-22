import io.restassured.response.Response;
import org.example.Order;
import org.example.ScooterApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    @Parameterized.Parameter
    public Order order;
    private ScooterApi api;
    private String track;

    @Before
    public void setUp(){
        api = new ScooterApi();
    }

    @After
    public void tearDown(){
        if (track != null){
            Response response = api.cancelOrder(track);
            response.then().statusCode(200);
            response.then().body("ok", equalTo(true));
        }
    }

    @Parameterized.Parameters
    public static Object[][] testOrderData(){
        return new Object[][] {
                {new Order("Vasiliy", "Ivanov", "Tverskaya 5", "5", "88005553535",
                        3, "2024-12-12", "left door", new String[]{"BLACK"})},
                {new Order("Alexander", "Knyazev", "Lubyanka 2", "4", "88005653739",
                        4, "2025-01-01", "no comments", new String[]{"GREY"})},
                {new Order("Anton", "Sidorov", "Moskovskiy pr 13", "5", "8495663536",
                        1, "2024-05-11", "right door", new String[]{"BLACK", "GREY"})},
                {new Order("Dimon", "Zaytsev", "Lev Yashin 7", "9", "74957773535",
                        2, "2024-07-20", "don't disturb", new String[]{})}

        };
    }

    @Test
    public void createOrderTest(){
        Response response = api.createOrder(order);
        response.then().statusCode(201);
        response.then().body("track", notNullValue());
        track = response.then().extract().path("track").toString(); //получаем id для последующего удаления заказа
    }
}
