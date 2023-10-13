package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    OrderController orderController;
    // mock a repo
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setupBeforeTest() {
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemForTest");
        item.setDescription("Test Description");
        item.setPrice(BigDecimal.valueOf(10));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.addItem(item);
        cart.setTotal(BigDecimal.valueOf(20));


        User user = new User();
        user.setUsername("userForTest");
        user.setPassword("password12345");
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findByUsername("userForTest")).thenReturn(user);
    }

    @Test
    @Transactional
    public void submitTest() {
        ResponseEntity<UserOrder> order = orderController.submit("userForTest");
        Assert.assertNotNull(order);
        Assert.assertEquals(200, order.getStatusCodeValue());
        Assert.assertEquals("userForTest", order.getBody().getUser().getUsername());
        Assert.assertEquals(2, order.getBody().getItems().size());
        Assert.assertEquals(BigDecimal.valueOf(20), order.getBody().getTotal());
    }

    @Test
    public void getOrderForUserTest() {
        UserOrder userorder = orderController.submit("userForTest").getBody();
        when(orderRepository.findByUser(userRepository.findByUsername("userForTest"))).thenReturn(Arrays.asList(userorder));
        ResponseEntity<List<UserOrder>> userOrders = orderController.getOrdersForUser("userForTest");
        Assert.assertNotNull(userOrders);
        Assert.assertEquals(200, userOrders.getStatusCodeValue());
        Assert.assertEquals(1, userOrders.getBody().size());
    }
}
