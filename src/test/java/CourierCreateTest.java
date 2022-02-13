import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
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
    private int expectedCodResponseCreatedCourierSuccess = 201;
    private int actualCodResponseCreatedCourier;
    private Response response;
    private String testLogin = faker.name().username();
    private String password = faker.number().digits(10);
    private String firstName = faker.name().firstName();
    Map<String,Boolean> mapExpected = new HashMap<>();
    Map<String,Boolean> mapActual = new HashMap<>();



    @Before
    public void setUp(){
        courierClient =  new CourierClient();
        courier = Courier.getRandom();
        //courierDoubleOne = new Courier (testLogin,password,firstName);
        //courierDoubleTwo = new Courier (testLogin,password,firstName);
    }

    @After
    public void tearDown(){
        actualCodResponseCreatedCourier = response.statusCode();
        if (actualCodResponseCreatedCourier == expectedCodResponseCreatedCourierSuccess){
            int courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
            courierClient.delete(courierId);}
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void courierCreateSuccessTest() {
        response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create", isCourierCreated, response.then().extract().path("ok"));
    }



    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void courierFailedCreateDoubleCourierTest(){
        String messageDoubleCourier = "Этот логин уже используется";
        response = courierClient.create(courier);
        Response responseCreateDoubleCourier = courierClient.create(new Courier(courier.login,courier.password,courier.firstName));
        assertEquals("Courier is not create",messageDoubleCourier,responseCreateDoubleCourier.then().extract().path("message"));
    }



    @Test
    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void courierCreateWithRequiredFieldsTest(){
        courier = Courier.getRandomRequiredFields();
        response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create",isCourierCreated,response.then().extract().path("ok"));
    }



    @Test
    @DisplayName("Запрос возвращает правильный код ответа")
    public void courierCreateSuccessCorrectCodResponseTest(){
        int codResponseSuccessCreate=201;
        response = courierClient.create(courier);
        assertEquals("Courier is not create, response cod not 201",codResponseSuccessCreate,response.statusCode());
    }



    @Test
    @DisplayName("Курьера можно создать. Тело ответа ok:true")
    public void courierCreateSuccessTrueInResponseTest(){
        mapExpected.put("ok", true);
        response = courierClient.create(courier);
        mapActual = new Gson().fromJson(response.body().asString(),mapExpected.getClass());
        assertEquals("Courier is not create",mapExpected,mapActual);
    }


    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку")
    public void courierFailedCreateWithNotAllRequiredFiedsTest(){
        String courierCreateWithNotAllRequiredFieds = "Недостаточно данных для создания учетной записи";
        courier = new Courier("",RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        response = courierClient.create(courier);
        assertEquals("Courier is not create",courierCreateWithNotAllRequiredFieds,response.then().extract().path("message"));
    }


    @Test
    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void courierFailedCreateDoubleLoginTest(){
        String loginDouble = faker.name().username();
        String courierCreateDoubleLogin = "Этот логин уже используется";
        courier = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Courier courierWithDoubleLogin = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        response = courierClient.create(courier);
        Response responseCourierWithDoubleLogin = courierClient.create(courierWithDoubleLogin);
        assertEquals("Courier is not create",courierCreateDoubleLogin,responseCourierWithDoubleLogin.then().extract().path("message"));
    }



}
