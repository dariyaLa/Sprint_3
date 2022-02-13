import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.Courier;
import ru.yandex.practikum.model.CourierCredentials;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CourierAuthTest {

    private CourierClient courierClient;
    CourierCredentials courierCredentials;
    private Courier courier;
    private int courierId;
    private int actualCodResponseLoginCourier;
    private int expectedCodResponseLoginCourierSuccess=200;
    private Response response;
    private Response responseLogin;
    private Faker faker = new Faker();

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
        response = courierClient.create(courier);
        courierCredentials = new CourierCredentials();
    }

    @After
    public void tearDown()
    {
        actualCodResponseLoginCourier = responseLogin.statusCode();
        if(actualCodResponseLoginCourier == expectedCodResponseLoginCourierSuccess){
            courierId=responseLogin.path("id");
            courierClient.delete(courierId);
        }
    }


    @Test
    @DisplayName("Курьер может авторизоваться, успешный запрос возвращает id")
    public void courierAuthSuccessTest(){
        responseLogin = courierClient.login(CourierCredentials.from(courier));
        courierId = responseLogin.path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));
    }


    @Test
    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль")
    public void courierAuthFailedWithIncorrectPasswordTest(){
        String passwordIncorect = faker.number().digits(10);
        String messageAccNotFound = "Учетная запись не найдена";
        responseLogin = courierClient.login(CourierCredentials.from(new Courier(courier.login,passwordIncorect,courier.firstName)));
        assertEquals("Error",messageAccNotFound,responseLogin.then().extract().path("message"));
    }


    @Test
    @DisplayName("Если какого-то поля нет (логин - пустая строка), запрос возвращает ошибку")
    public void courierAuthFailedEmptyLoginTest(){
        String messageNotAllData = "Недостаточно данных для входа";
        courier = new Courier("",courier.password);
        responseLogin = courierClient.login(CourierCredentials.from(courier));
        assertEquals("Error",messageNotAllData, responseLogin.then().extract().path("message"));
    }


    @Test
    @DisplayName("Если какого-то поля нет (пароль - пустая строка), запрос возвращает ошибку")
    public void courierAuthFailedEmptyPasswordTest(){
        String messageNotAllData = "Недостаточно данных для входа";
        courier = new Courier(courier.login,"");
        responseLogin = courierClient.login(CourierCredentials.from(courier));
        assertEquals("Error",messageNotAllData, responseLogin.then().extract().path("message"));
    }


    @Test
    @DisplayName("Если какого-то поля нет (пароля), запрос возвращает ошибку")
    public void courierAuthFailedWithoutPasswordTest(){
        courier.setLogin(faker.name().username());
        courierCredentials.setLogin(courier.getLogin());
        responseLogin = courierClient.login(courierCredentials);
        assertEquals("Error",expectedCodResponseLoginCourierSuccess, responseLogin.statusCode());
    }


    @Test
    @DisplayName("Если какого-то поля нет (логина), запрос возвращает ошибку")
    public void courierAuthFailedWithoutLoginTest(){
        String messageNotAllData = "Недостаточно данных для входа";
        courier.setPassword(faker.number().digits(10));
        courierCredentials.setPassword(courier.getPassword());
        responseLogin = courierClient.login(courierCredentials);
        assertEquals("Error",messageNotAllData, responseLogin.then().extract().path("message"));
    }


    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void courierAuthFailedNonExistentCourierTest(){
        String loginNonExistentCourier = faker.name().username();
        String passwordNonExistentCourier = faker.number().digits(10);
        String firstNameNonExistentCourier = faker.name().firstName();
        String messageAccNotFound = "Учетная запись не найдена";
        courier = new Courier(loginNonExistentCourier,passwordNonExistentCourier,firstNameNonExistentCourier);
        responseLogin = courierClient.login(CourierCredentials.from(courier));
        assertEquals("Error",messageAccNotFound,responseLogin.then().extract().path("message"));

    }


}
