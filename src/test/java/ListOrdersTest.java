import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.model.Orders;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

public class ListOrdersTest {


    private CourierClient courierClient;
    private Orders orders;

    @Before
    public void setUp(){
        courierClient =  new CourierClient();
        orders = new Orders("test","test","test","test","test",6,"test","test", new String[]{});

    }

    @DisplayName("В тело ответа возвращается список заказов")
    @Test
    public void getOrders (){
        Response response = courierClient.orders(orders);
        int track = response.then().extract().path("track");
        assertThat("Orders is not create, not found track",track,is(not(0)));
        Response response1 = courierClient.getOrders();
        assertThat("Order not found",response1.then().extract().path("orders"),not(empty()));
    }
}
