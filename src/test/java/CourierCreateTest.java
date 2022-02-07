import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.Courier;
import static org.junit.Assert.assertEquals;


public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courierDouble1;
    private Courier courierDouble2;
    //private int courierId;
    private String testLogin = "testLogin2.0";
    private String password = "123456";
    private String firstName = "firstName";

    @Before
    public void setUp(){
        courierClient =  new CourierClient();
        courierDouble1 = new Courier (testLogin,password,firstName);
        courierDouble2 = new Courier (testLogin,password,firstName);
    }


    @DisplayName("Курьера можно создать")
    @Test
    public void courierCreateTest(){
        Courier courier = Courier.getRandom();
        Response response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create",isCourierCreated,response.then().extract().path("ok"));
        courierClient.deleteCourier(courier);
    }


    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Test
    public void courierNotCreateDoubleCourierTest(){
        String messageDoubleCourier = "Этот логин уже используется";
        Response response1 = courierClient.create(courierDouble1);
        Response response2 = courierClient.create(courierDouble2);
        assertEquals("Courier is not create",messageDoubleCourier,response2.then().extract().path("message"));
        courierClient.deleteCourier(courierDouble1);
    }


    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля")
    @Test
    public void courierCreateWithRequiredFieldsTest(){
        Courier courier = Courier.getRandomRequiredFields();
        Response response = courierClient.create(courier);
        boolean isCourierCreated = true;
        assertEquals("Courier is not create",isCourierCreated,response.then().extract().path("ok"));
        courierClient.deleteCourier(courier);
    }


    @DisplayName("Запрос возвращает правильный код ответа")
    @Test
    public void courierCreateSuccessTest(){
        Courier courier = Courier.getRandom();
        int codResponse=201;
        Response response = courierClient.create(courier);
        assertEquals("Courier is not create, response cod not 201",codResponse,response.statusCode());
        courierClient.deleteCourier(courier);
    }


    @DisplayName("Курьера можно создать. Тело ответа ok:true")
    @Test
    public void courierCreateSuccess1Test(){
        Courier courier = Courier.getRandom();
        String responseCourierCreate="{\"ok\":true}";
        Response response = courierClient.create(courier);
        assertEquals("Courier is not create",responseCourierCreate,response.body().asString());
        courierClient.deleteCourier(courier);
    }


    @DisplayName("Если одного из полей нет, запрос возвращает ошибку")
    @Test
    public void courierCreateWithNotAllRequiredFiedsTest(){
        String courierCreateWithNotAllRequiredFieds = "Недостаточно данных для создания учетной записи";
        Courier courier = new Courier("",RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Response response = courierClient.create(courier);
        assertEquals("Courier is not create",courierCreateWithNotAllRequiredFieds,response.then().extract().path("message"));
    }


    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void courierCreateDoubleLoginTest(){
        String loginDouble = "loginDoubleTest";
        String courierCreateDoubleLogin = "Этот логин уже используется";
        Courier courier1 = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Courier courier2 = new Courier(loginDouble, RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        Response response1 = courierClient.create(courier1);
        Response response2 = courierClient.create(courier2);
        assertEquals("Courier is not create",courierCreateDoubleLogin,response2.then().extract().path("message"));
        courierClient.deleteCourier(courier1);
    }



}
