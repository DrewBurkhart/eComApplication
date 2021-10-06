package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.InjectObjects(userController, "userRepository", userRepo);
        TestUtils.InjectObjects(userController, "cartRepository", cartRepo);
        TestUtils.InjectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void validate_CreateUser() throws Exception {
        when(encoder.encode("password")).thenReturn("hashedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Andrew");
        r.setPassword("password");
        r.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Andrew", u.getUsername());
        assertEquals("hashedPassword", u.getPassword());
    }

    @Test
    public void validate_PasswordTooShortFails() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Andrew");
        r.setPassword("pass");
        r.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void validate_PasswordConfirmationNotMatchingFails() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Andrew");
        r.setPassword("password");
        r.setConfirmPassword("password1");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void validate_GetUserById() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("Andrew");
        user.setPassword("password");

        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<User> userResponse = userController.findById(id);
        User u = userResponse.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals("Andrew", u.getUsername());
    }

    @Test
    public void validate_GetUserByUserName() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("Andrew");
        user.setPassword("password");

        when(userRepo.findByUsername("Andrew")).thenReturn(user);
        ResponseEntity<User> userResponse = userController.findByUserName("Andrew");
        User u = userResponse.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals("Andrew", u.getUsername());
    }
}
