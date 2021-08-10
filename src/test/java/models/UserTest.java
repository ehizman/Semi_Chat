package models;

import exceptions.UserDoesNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private UserService userService;
    @BeforeEach
    void setUp(){
        userService = UserService.getInstance();
    }
    @Test
    void test_Constructor(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "ehizman@gmail.com");
        assertAll(
                ()-> assertEquals("Ehis", user.getFirstName()),
                () -> assertEquals("Edemakhiota", user.getLastName()),
                () -> assertEquals("ehizman@gmail.com", user.getEmail()),
                ()-> assertEquals("String", user.getId().getClass().getSimpleName())
        );
    }

    @Test
    void test_userCanRegister(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        assertTrue(userService.getUserDatabase().contains(user));
        assertEquals(userService.getUserDatabase().size(), 2);
    }

    @Test
    void test_userCanFindOtherUsers(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        User user3 = userService.registerNative("Eseosa", "Nathan", "eseosaedemakhiota@gmail.com");

        List<User> usersThatHaveSuppliedFieldInName = userService.find("Eseosa");
        assertAll(
                () -> assertTrue(usersThatHaveSuppliedFieldInName.contains(user2)),
                () -> assertTrue(usersThatHaveSuppliedFieldInName.contains(user3)),
                () -> assertTrue(usersThatHaveSuppliedFieldInName.contains(user)),
                () -> assertEquals(usersThatHaveSuppliedFieldInName.size(), 3)
        );
    }

    @Test
    void test_UserNotFoundException_WhenUserDatabaseDoesNotContainNamePatternSuppliedByUser(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        assertThrows(UserDoesNotExistException.class, ()-> userService.find("Ehis"));
    }

    @Test
    void test_UserCanSendFriendRequests(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        userService.sendFriendRequest
    }

    @AfterEach
    void tearDown(){
        userService.getUserDatabase().deleteAll();
    }
}