package com.example.demo;

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.OrderControllerTest;
import com.example.demo.controllers.UserControllerTest;
import com.example.demo.repositories.CartRepositoryTest;
import com.example.demo.repositories.ItemRepositoryTest;
import com.example.demo.repositories.OrderRepositoryTest;
import com.example.demo.repositories.UserRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CartControllerTest.class,
        CartRepositoryTest.class,
        UserControllerTest.class,
        UserRepositoryTest.class,
        OrderControllerTest.class,
        OrderRepositoryTest.class,
        ItemControllerTest.class,
        ItemRepositoryTest.class,
})
public class SareetaApplicationTests {

    @Test
    public void contextLoads() {
    }

}
