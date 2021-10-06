package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTests {

    private OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.InjectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.InjectObjects(orderController, "userRepository", userRepo);

        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("Andrew");
        user.setPassword("password");

        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));

        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(Arrays.asList(item));
        cart.setUser(user);
        cart.setTotal(new BigDecimal(1));
        user.setCart(cart);

        when(userRepo.findByUsername("Andrew")).thenReturn(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(Arrays.asList(item));
        when(orderRepo.findByUser(user)).thenReturn(Arrays.asList(userOrder));
    }

    @Test
    public void validate_Submit() {
        ResponseEntity<UserOrder> response = orderController.submit("Andrew");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void validate_History() {
        ResponseEntity<UserOrder> response = orderController.submit("Andrew");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> historyResponse = orderController.getOrdersForUser("Andrew");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = historyResponse.getBody();
        assertNotNull(orders);
        UserOrder order = orders.get(0);
        assertEquals("Andrew", order.getUser().getUsername());
        assertEquals(1, order.getItems().size());
    }
}
