package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUserTest() {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Username");
        request.setPassword("Password1234");
        request.setConfirmPassword("Password1234");

        ResponseEntity<User> response = userController.createUser(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals("Username", Objects.requireNonNull(response.getBody()).getUsername());
        Assert.assertNotEquals("Password1234", response.getBody().getPassword());

        /* Testing password is too short */

        CreateUserRequest request2 = new CreateUserRequest();
        request2.setUsername("UsernameSortPassword");
        request2.setPassword("1");
        request2.setConfirmPassword("1");

        ResponseEntity<User> response2 = userController.createUser(request2);
        Assert.assertNotNull(response2);
        // Check http-statuscode 400
        Assert.assertEquals(400, response2.getStatusCodeValue());
    }

    @Test
    public void findByIdTest() {

        User user = new User();
        user.setUsername("John");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User user1 = userController.findById(1L).getBody();
        Assert.assertEquals(user.getId(), user1.getId());
        Assert.assertEquals(user.getUsername(), user1.getUsername());
    }

    @Test
    @Transactional
    public void findByUserNameTest() {

        User user = new User();
        user.setId(1L);
        user.setUsername("Doe");
        when(userRepository.findByUsername("Doe")).thenReturn(user);

        User user1 = userController.findByUserName("Doe").getBody();
        Assert.assertNotNull(user1);
        Assert.assertEquals(user.getUsername(), user1.getUsername());
    }
}
