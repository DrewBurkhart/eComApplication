package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    private CartController cartController;

    private CartRepository cartRepo = mock(CartRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.InjectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.InjectObjects(cartController, "userRepository", userRepo);
        TestUtils.InjectObjects(cartController, "itemRepository", itemRepo);

        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("Andrew");
        user.setPassword("password");
        user.setCart(new Cart());
        when(userRepo.findByUsername("Andrew")).thenReturn(user);

        Item item = new Item();
        item.setId(id);
        item.setName("Test");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(1));
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
    }

    @Test
    public void validate_AddToCart() throws Exception {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setQuantity(3);
        r.setItemId(1);
        r.setUsername("Andrew");

        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void validate_RemoveFromCart() throws Exception {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setQuantity(3);
        r.setItemId(1);
        r.setUsername("Andrew");

        ResponseEntity<Cart> response = cartController.removeFromcart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
