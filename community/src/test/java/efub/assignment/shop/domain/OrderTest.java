package efub.assignment.shop.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private OrderItem item;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .status(OrderStatus.ORDER)
                .item(new OrderItem("연필", 1000, 3))
                .build();
    }

    @Test
    void 주문을_취소하면_CANCEL상태가된다() {
        Order order = new Order();
        order.cancel();
        assertEquals(OrderStatus.CANCEL, order.getStatus());
    }

    @Test
    void 이미_취소된_주문을_다시_취소하면_예외() {
        Order order = new Order();
        order.cancel();
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void 주문총액은_OrderItem_합계다() {
        assertEquals(3000, order.calculateTotalPrice());
    }

}