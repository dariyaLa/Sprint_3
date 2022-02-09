import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class CourierCreateTest {

    private Faker faker = new Faker();
    private CourierClient courierClient;
    private Courier courier;
    private Courier courierDouble;
    private String testLogin = faker.name().username();
    private String password = faker.number().digits(10);
    private String firstName = faker.name().firstName();
    Map<String,Boolean> mapExpected = new HashMap<>();
    Map<String,Boolean> mapActual = new HashMap<>();



    @Before
    public void setUp(){
        courierClient =  new CourierClient();
        courier = new Courier (testLogin,password,firstName);
        courierDouble = new Courier (testLogin,password,firstName);
    }


    @DisplayName("Курьера можно создать")
    @Test
    public void courierCreateSuccessTest(){
        Courier courier = Courier.getRandom();
        Response response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create",isCourierCreated,response.then().extract().path("ok"));
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }


    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Test
    public void courierFailedCreateDoubleCourierTest(){
        String messageDoubleCourier = "Этот логин уже используется";
        Response response = courierClient.create(courier);
        Response responseDouble = courierClient.create(courierDouble);
        assertEquals("Courier is not create",messageDoubleCourier,responseDouble.then().extract().path("message"));
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }


    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    @Test
    public void courierCreateWithRequiredFieldsTest(){
        Courier courier = Courier.getRandomRequiredFields();
        Response response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create",isCourierCreated,response.then().extract().path("ok"));
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }


    @DisplayName("Запрос возвращает правильный код ответа")
    @Test
    public void courierCreateSuccessCorrectCodResponseTest(){
        Courier courier = Courier.getRandom();
        int codResponse=201;
        Response response = courierClient.create(courier);
        assertEquals("Courier is not create, response cod not 201",codResponse,response.statusCode());
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }


    @DisplayName("Курьера можно создать. Тело ответа ok:true")
    @Test
    public void courierCreateSuccessTrueInResponseTest(){
        Courier courier = Courier.getRandom();
        mapExpected.put("ok", true);
        Response response = courierClient.create(courier);
        mapActual = new Gson().fromJson(response.body().asString(),mapExpected.getClass());
        assertEquals("Courier is not create",mapExpected,mapActual);
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }


    @DisplayName("Если одного из полей нет, запрос возвращает ошибку")
    @Test
    public void courierFailedCreateWithNotAllRequiredFiedsTest(){
        String courierCreateWithNotAllRequiredFieds = "Недостаточно данных для создания учетной записи";
        Courier courier = new Courier("",RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Response response = courierClient.create(courier);
        assertEquals("Courier is not create",courierCreateWithNotAllRequiredFieds,response.then().extract().path("message"));
    }


    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void courierFailedCreateDoubleLoginTest(){
        String loginDouble = faker.name().username();
        String courierCreateDoubleLogin = "Этот логин уже используется";
        Courier courier = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Courier courierWithDoubleLogin = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Response response = courierClient.create(courier);
        Response responseCourierWithDoubleLogin = courierClient.create(courierWithDoubleLogin);
        assertEquals("Courier is not create",courierCreateDoubleLogin,responseCourierWithDoubleLogin.then().extract().path("message"));
        courierClient.delete(courierClient.login(CourierCredentials.from(courier)).then().extract().path("id"));
    }



}
