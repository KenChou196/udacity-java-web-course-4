package com.example.demo.controllers;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {
    @Autowired
    private CartController cartController;
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void initUserForTest() {
        // Create user for test
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("userForTest");
        createUserRequest.setPassword("Password1234");
        createUserRequest.setConfirmPassword("Password1234");
        // mock handle create user, cart request
        userController.createUser(createUserRequest).getBody();
    }
    @Test
    @Transactional
    public void addToCartTest() {
        // create modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("userForTest");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        Cart cart = cartController.addTocart(modifyCartRequest).getBody();

        Assert.assertEquals(1, cart != null ? cart.getItems().size() : 0);
        // Test for unsuccessful

        ModifyCartRequest modifyCartRequestFail = new ModifyCartRequest();
        modifyCartRequestFail.setUsername("notExistUser");
        modifyCartRequestFail.setQuantity(2);
        modifyCartRequestFail.setItemId(1);
        // Check request return http-status 404
        Assert.assertEquals(404, cartController.addTocart(modifyCartRequestFail).getStatusCodeValue());

    }
    // Test for removeFromCart()
    @Test
    @Transactional
    public void removeFromCartTest() {
        // add to cart
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("userForTest");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        Cart cart = cartController.addTocart(modifyCartRequest).getBody();
        Assert.assertEquals(2, cart != null ? cart.getItems().size() : 0);

        // removeFromCart
        ModifyCartRequest removeFromCart = new ModifyCartRequest();
        removeFromCart.setUsername("userForTest");
        removeFromCart.setItemId(1);
        // set quantity 2 => 1
        removeFromCart.setQuantity(1);
        cartController.removeFromcart(removeFromCart).getBody();
        User user = userRepository.findByUsername("userForTest");
        Assert.assertEquals(1, user != null ? user.getCart().getItems().size() : 0);
    }
}
