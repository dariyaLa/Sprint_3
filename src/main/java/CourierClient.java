import io.restassured.response.Response;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;
import ru.yandex.practikum.model.Orders;

import static io.restassured.RestAssured.given;

public class CourierClient extends CourierRestClient{

    public final String COURIER_PATH = BASE_URL + "/courier/";
    public final String ORDERS_PATH = BASE_URL + "/orders/";

    public Response create (Courier courier){
        Response response = given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
        return response;
    }

    public Response login (CourierCredentials courierCredentials){
        Response response = given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_PATH+"login");
        return response;
    }

    public int loginGetId (CourierCredentials courierCredentials){
        int response = given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_PATH+"login")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
        return response;
    }

    public boolean delete (int courierId){
        return  given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH+courierId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }

    public Response orders (Orders orders){
        Response response = given()
                .spec(getBaseSpec())
                .when()
                .post(ORDERS_PATH);
        return  response;
    }

    public Response getOrders (){
        Response response = given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
        return  response;
    }



}
