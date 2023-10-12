package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemController itemController;

    @Test
    @Transactional
    public void getItemsTest() {
        // get list Items
        ResponseEntity<List<Item>> items = itemController.getItems();
        Assert.assertNotNull(items);
        Assert.assertEquals(200, items.getStatusCodeValue());
    }

    @Test
    @Transactional
    public void getItemByIdTest() {
        Item item = itemController.getItemById(1l).getBody();
        Item itemId = Objects.requireNonNull(item);
        Item itemRef = itemRepository.getOne(1L);
        // password
        Assert.assertTrue(itemId.hashCode() == itemRef.hashCode());
        // id
        Assert.assertTrue(itemId.equals(itemRef));
    }

    @Test
    @Transactional
    public void getItemsByName() {
        // name at data.sql
        List<Item> items = itemController.getItemsByName("Intel core I9").getBody();
        List<Item> itemsRef = itemRepository.findByName("Intel core I9");
        Assert.assertTrue(items.get(0).equals(itemsRef.get(0)));
    }
}
