package com.example.demo.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
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
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Test
    @Transactional
    public void findByUserTest(){
        // create user
        User user = new User();
        user.setUsername("John_Doe");
        user.setPassword("Password");
        // create Item
        Item item = new Item();
        item.setName("AMD_Ryzen9");
        item.setDescription("Super chip set for computer");
        item.setPrice(BigDecimal.valueOf(10000));
        // create Cart
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        user.setCart(cart);
        // save to db
        cartRepository.save(cart);
        userRepository.save(user);

        User user1 = userRepository.findByUsername("John_Doe");
        Cart foundCart = cartRepository.findByUser(user1);

        Assert.assertNotNull(foundCart);
        Assert.assertEquals("Super chip set for computer",cart.getItems().get(0).getDescription());
        Assert.assertEquals("AMD_Ryzen9", cart.getItems().get(0).getName());
        // negative test
        User unknown = new User();
        unknown.setUsername("unknown");
        Cart unknownCart = cartRepository.findByUser(unknown);
        Assert.assertNull(unknownCart);
    }
}
