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
    private Courier courier;
    private int courierId;
    private Response response;
    private boolean isCourierCreated = true;


    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
        response = courierClient.create(courier);
    }

    @After
    public void tearDown()
    {
        courierClient.delete(courierId);
    }

    @DisplayName("Курьер может авторизоваться")
    @Test
    public void courierAuthSuccessTest(){
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));

    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль")
    @Test
    public void courierAuthFailedTest(){
        String passwordIncorect = "00000";
        String messageAccNotFound = "Учетная запись не найдена";
        Response responsepasswordFailed = courierClient.login(CourierCredentials.from(new Courier(courier.login,passwordIncorect,courier.firstName)));
        assertEquals("Error",messageAccNotFound,responsepasswordFailed.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));

    }


    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    @Test
    public void courierAuthFailedNotAllDataTest(){
        String messageNotAllData = "Недостаточно данных для входа";
        Response response = courierClient.login(CourierCredentials.from(new Courier(courier.password)));
        assertEquals("Error",messageNotAllData, response.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));
    }



    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void courierAuthNonexistentCourierTest(){
        String loginNonexistentCourier = "00000";
        String passwordNonexistentCourier = "00000";
        String firstNameNonexistentCourier = "00000";
        String messageAccNotFound = "Учетная запись не найдена";
        Courier courierNonexistent = new Courier(loginNonexistentCourier,passwordNonexistentCourier,firstNameNonexistentCourier);
        Response response = courierClient.login(CourierCredentials.from(courierNonexistent));
        assertEquals("Error",messageAccNotFound,response.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));

    }


    @DisplayName("Успешный запрос возвращает id")
    @Test
    public void courierLoginTest(){
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier id is incorrect",courierId,is(not(0)));

    }



}
