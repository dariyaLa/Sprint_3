import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;
import ru.yandex.practikum.model.Orders;

import static io.restassured.RestAssured.given;

public class CourierClient extends CourierRestClient{

    public final String COURIER_PATH = BASE_URL + "/courier/";
    public final String ORDERS_PATH = BASE_URL + "/orders/";

    @Step("Create courier {courier}")
    public Response create (Courier courier){
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(courier)
                .when()
                .post(COURIER_PATH);
        return response;
    }

    @Step("Login as {courierCredentials}")
    public Response login (CourierCredentials courierCredentials){
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(courierCredentials)
                .when()
                .post(COURIER_PATH+"login");
        return response;
    }

    @Step("Delete courier with courierId={courierId}")
    public boolean delete (int courierId){
        return  given()
                .spec(getBaseSpec())
                .log().all()
                .when()
                .delete(COURIER_PATH+courierId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }

    @Step("Create order")
    public Response orders (Orders orders){
        Response response = given()
                .spec(getBaseSpec())
                .when()
                .post(ORDERS_PATH);
        return  response;
    }

    @Step("Get list orders")
    public Response getOrders (){
        Response response = given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
        return  response;
    }



}
