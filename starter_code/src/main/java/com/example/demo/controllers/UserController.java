package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// log library here
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        if (createUserRequest.getPassword().length() < 8) {
            System.out.println("Error - Either length is less than 8 ");
            logger.error("Password create for user " + createUserRequest.getUsername() + " is to short");
            return ResponseEntity.badRequest().build();

        }
        if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            System.out.println("Error - pass and conf pass do not match. Unable to create ");
            logger.error("Duplicate user with:  " + createUserRequest.getUsername() + " name");
            return ResponseEntity.badRequest().build();
        }
        User user = new User();
        Cart cart = new Cart();
        // create username and password hash
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        // set cart for user
        user.setCart(cart);
        // set user for cart
        cart.setUser(user);
        // defaut total is 0
        cart.setTotal(BigDecimal.valueOf(0));
        // save to Database
        userRepository.save(user);
        cartRepository.save(cart);
        logger.info("User " + user.getUsername() + " have been created ");
        return ResponseEntity.ok(user);
    }

}
