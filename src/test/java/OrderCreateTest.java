import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practikum.model.Orders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final String[] color;
    private final int expectedCodResponse;

    public CourierClient courierClient;
    public Orders orders;

    public OrderCreateTest (String[] color, int expectedCodResponse){
        this.color=color;
        this.expectedCodResponse=expectedCodResponse;
    }


    @Before
    public void setUp() {
        courierClient = new CourierClient();

    }


    @DisplayName("Сделать заказ с различными цветами - параметризация")
    @Test
    public void orderCreateWithColor (){
        orders = Orders.newOrder(color);
        Response response = courierClient.orders(orders);
        int actualCodResponse = response.statusCode();
        assertEquals("Error", expectedCodResponse, actualCodResponse);

    }

    @Parameterized.Parameters
    public static Object[][] dataGen() {
        return new Object[][]{
                {new String[]{"GREY"},201},
                {new String[]{"BlACK"},201},
                {new String[]{"GREY","BlACK"},201},
                {new String[]{},201}
        };
    }

    @DisplayName("Создание заказа, в ответе есть track")
    @Test
    public void orderCreate(){
        Response response = courierClient.orders(orders);
        int isTrack = response.then().extract().path("track");
        assertThat("Orders is not create, not found track",isTrack,is(not(0)));
    }
}
