package com.example.demo.repositories;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Test
    @Transactional
    public void findByNameTest(){
        // create a Item
        Item item = new Item();
        item.setName("AMD_Ryzen9");
        item.setDescription("Super chip set for computer");
        item.setPrice(BigDecimal.valueOf(10000));
        itemRepository.save(item);
        // find item in db
        List<Item> foundItems = itemRepository.findByName("AMD_Ryzen9");
        Assert.assertNotNull(foundItems);
        // check name
        Assert.assertEquals("AMD_Ryzen9",foundItems.get(0).getName());
        // check description
        Assert.assertEquals("Super chip set for computer",foundItems.get(0).getDescription());
        // check prices
        Assert.assertEquals(BigDecimal.valueOf(10000),foundItems.get(0).getPrice());
        // check id
        Assert.assertNotNull(foundItems.get(0).getId());
        // find item non exist return List with length = 0;
        List<Item> nonExistItems = itemRepository.findByName("nonExistItems");
        Assert.assertEquals(0,nonExistItems.size());

    }
}
