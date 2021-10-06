package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.InjectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void validate_GetAllItems() throws Exception {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));

        when(itemRepo.findAll()).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        Item returnedItem = items.get(0);
        assertEquals(id, returnedItem.getId());
        assertEquals("Test", returnedItem.getName());
        assertEquals("Test Description", returnedItem.getDescription());
        assertEquals(new BigDecimal(1), returnedItem.getPrice());
    }

    @Test
    public void validate_GetItemById() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));

        when(itemRepo.findById(id)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returnedItem = response.getBody();
        assertEquals(id, returnedItem.getId());
        assertEquals("Test", returnedItem.getName());
        assertEquals("Test Description", returnedItem.getDescription());
        assertEquals(new BigDecimal(1), returnedItem.getPrice());
    }

    @Test
    public void validate_BadItemIdFails() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void validate_GetItemByName() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));

        when(itemRepo.findByName("Test")).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        Item returnedItem = items.get(0);
        assertEquals(id, returnedItem.getId());
        assertEquals("Test", returnedItem.getName());
        assertEquals("Test Description", returnedItem.getDescription());
        assertEquals(new BigDecimal(1), returnedItem.getPrice());
    }
}
