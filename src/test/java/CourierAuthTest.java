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
    private Courier courier;
    private int courierId;
    private Response response;
    private boolean isCourierCreated = true;
    private Faker faker = new Faker();

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

    @DisplayName("Курьер может авторизоваться, успешный запрос возвращает id")
    @Test
    public void courierAuthSuccessTest(){
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
        assertThat("Courier auth failed",courierId, is(not(0)));

    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль")
    @Test
    public void courierAuthFailedWithIncorrectPasswordTest(){
        String passwordIncorect = faker.number().digits(10);
        String messageAccNotFound = "Учетная запись не найдена";
        Response responsepasswordFailed = courierClient.login(CourierCredentials.from(new Courier(courier.login,passwordIncorect,courier.firstName)));
        assertEquals("Error",messageAccNotFound,responsepasswordFailed.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
    }


    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    @Test
    public void courierAuthFailedWithoutPasswordTest(){
        String messageNotAllData = "Недостаточно данных для входа";
        Response response = courierClient.login(CourierCredentials.from(new Courier(courier.password)));
        assertEquals("Error",messageNotAllData, response.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");
    }



    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void courierAuthFailedNonExistentCourierTest(){
        String loginNonExistentCourier = faker.name().username();
        String passwordNonExistentCourier = faker.number().digits(10);
        String firstNameNonExistentCourier = faker.name().firstName();
        String messageAccNotFound = "Учетная запись не найдена";
        Courier courierNonExistent = new Courier(loginNonExistentCourier,passwordNonExistentCourier,firstNameNonExistentCourier);
        Response response = courierClient.login(CourierCredentials.from(courierNonExistent));
        assertEquals("Error",messageAccNotFound,response.then().extract().path("message"));
        courierId = courierClient.login(CourierCredentials.from(courier)).then().extract().path("id");

    }


}
