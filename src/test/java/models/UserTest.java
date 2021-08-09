package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private UserService userService;
    @BeforeEach
    void setUp(){
        userService = UserService.createSingletonOfUserService();
    }
    @Test
    void test_constructor(){
        User user = new Native("Ehis", "Edemakhiota", "ehizman@gmail.com");
        assertAll(
                ()-> assertEquals("Ehis", user.getFirstName()),
                () -> assertEquals("Edemakhiota", user.getLastName()),
                () -> assertEquals("ehizman@gmail.com", user.getEmail()),
                ()-> assertTrue(user.getId().getClass().getSimpleName().equals("String"))
        );
    }

    @Test
    void test_user_can_register(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        assertTrue(userService.getUserDatabase().contains(user));
        assertEquals(userService.getUserDatabase().size(), 2);
    }

    @Test
    void test_persistence(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com");
        assertEquals(userService.getUserDatabase().size(), 1);
    }

    @AfterEach
    void tearDown(){
        userService.getUserDatabase().deleteAll();
    }
}