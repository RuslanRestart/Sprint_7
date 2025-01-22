import io.restassured.response.Response;
import org.example.ScooterApi;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {

    private ScooterApi api = new ScooterApi();

    @Test
    public void getOrderTest(){
        Response ordersListResponse = api.getOrdersList();
        ordersListResponse.then().statusCode(200);
        ordersListResponse.then()
                .body("orders", notNullValue())
                .body("orders[0].id", notNullValue());
        ordersListResponse.prettyPrint();
    }

}
