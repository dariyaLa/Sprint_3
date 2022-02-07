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

    private final Orders orders;
    private final int expectedCodResponse;

    public CourierClient courierClient;

    public OrderCreateTest (Orders orders, int expectedCodResponse){
        this.orders=orders;
        this.expectedCodResponse=expectedCodResponse;
    }


    @Before
    public void setUp() {
        courierClient = new CourierClient();

    }


    @DisplayName("Сделать заказ - параметризация")
    @Test
    public void orderCreateWithColor (){
        Response response = courierClient.orders(orders);
        int actualCodResponse = response.statusCode();
        assertEquals("Error", expectedCodResponse, actualCodResponse);

    }

    @Parameterized.Parameters
    public static Object[][] dataGen() {
        return new Object[][]{
                {new Orders("test","test","test","test","test",6,"test","test", new String[]{"GREY"}),201},
                {new Orders("test","test","test","test","test",6,"test","test", new String[]{"BLACK"}),201},
                {new Orders("test","test","test","test","test",6,"test","test", new String[]{"GREY","BLACK"}),201},
                {new Orders("test","test","test","test","test",6,"test","test", new String[]{}),201}
        };
    }

    @DisplayName("Создание заказа, в ответе есть track")
    @Test
    public void orderCreate(){
        Response response1 = courierClient.orders(orders);
        int isTrack = response1.then().extract().path("track");
        assertThat("Orders is not create, not found track",isTrack,is(not(0)));
    }
}
