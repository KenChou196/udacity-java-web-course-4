package com.example.demo.repositories;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserTest {
    @Autowired
    UserRepository userRepository;
    @Test
    @Transactional
    public void findByUsernameTest() {
        // happy case, create with validation infor
        User user = new User();
        user.setUsername("kynk1");
        user.setPassword("kynk1@123456");
        userRepository.save(user);
        User foundUser = userRepository.findByUsername("kynk1");
        Assert.assertNotNull(foundUser);
        Assert.assertEquals("kynk1", foundUser.getUsername());
        Assert.assertEquals("kynk1@123456", foundUser.getPassword());
        Assert.assertNotNull(foundUser.getId());
        // find non exist user return null
        User nonExistUser = userRepository.findByUsername("nonExistUser");
        Assert.assertNull(nonExistUser);
    }
}
