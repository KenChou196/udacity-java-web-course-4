package com.example.demo.repositories;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void findByUserTest(){

        User user = new User();
        user.setUsername("Jon");
        user.setPassword(("Password1234"));

        Item item = new Item();
        item.setPrice(BigDecimal.valueOf(10));
        item.setName("Intel Core I9");
        item.setDescription("Test Description");
        itemRepository.save(item);

        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        cart.setUser(user);

        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);

        UserOrder userOrder = UserOrder.createFromCart(cart);
        orderRepository.save(userOrder);

        List<UserOrder> foundOrders = orderRepository.findByUser(user);
        Assert.assertNotNull(foundOrders);
        Assert.assertEquals("Jon",foundOrders.get(0).getUser().getUsername());
        Assert.assertEquals("Intel Core I9",foundOrders.get(0).getItems().get(0).getName());
    }
}
