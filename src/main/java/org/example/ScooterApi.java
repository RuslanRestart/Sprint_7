package org.example;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ScooterApi extends BaseHttpClient{

    private final String apiPath = "api/v1/";

    public Response createCourier(Courier courier){
        return doPostRequest(apiPath + "courier", courier);
    }

    public Response loginCourier(String login, String password){
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);
        return doPostRequest(apiPath + "courier/login", credentials);
    }

    public Response createOrder(Order order){
        return doPostRequest(apiPath + "orders", order);
    }

    public Response deleteCourier(Integer courierId){
        return doDeleteRequest(apiPath + "courier/" + courierId);
    }

    public Response cancelOrder(String track){
        return doPutRequest(apiPath + "orders/cancel?track=" + track);
    }

    public Response getOrdersList(){
        return doGetRequest(apiPath + "orders");
    }


}
