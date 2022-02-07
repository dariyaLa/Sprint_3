import io.restassured.response.Response;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;
import ru.yandex.practikum.model.Orders;

import static io.restassured.RestAssured.given;

public class CourierClient extends CourierRestClient{

    public final String PATH = BASE_URL + "/courier/";
    public final String PATH2 = BASE_URL + "/orders/";

    public Response create (Courier courier){
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(courier)
                .when()
                .post(PATH);
        return response;
    }

    public Response login (CourierCredentials courierCredentials){
        Response response = given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(PATH+"login");
        return response;
    }

    public boolean delete (int courierId){
        return  given()
                .spec(getBaseSpec())
                .log().all()
                .when()
                .delete(PATH+courierId)
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
                .post(PATH2);
        return  response;
    }

    public Response getOrders (){
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .when()
                .get(PATH2);
        return  response;
    }


    public void deleteCourier (Courier courier){
        CourierClient courierClient = new CourierClient();
        Response responseLogin = courierClient.login(CourierCredentials.from(courier));
        try
        {responseLogin.then().extract().path("id");}
        catch(Exception exception)
        {System.out.println("Login failed");}
        try{
            int courierId = responseLogin.then().extract().path("id");
            courierClient.delete(courierId);}
        catch(Exception exception)
        {System.out.println("Courier not delete");}
    }

}
