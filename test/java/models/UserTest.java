package models;

import exceptions.FriendRequestException;
import exceptions.UserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseImpl;
import services.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private UserService userService;
    private DatabaseImpl<?> userDatabase;

    @BeforeEach
    void setUp(){
        userService = UserService.getInstance();
        userDatabase = DatabaseImpl.getInstance();
    }

    @Test
    void test_Constructor(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "ehizman@gmail.com", "Jesus123");
        assertAll(
                ()-> assertEquals("EHIS", user.getFirstName()),
                () -> assertEquals("EDEMAKHIOTA", user.getLastName()),
                () -> assertEquals("ehizman@gmail.com", user.getEmail()),
                ()-> assertEquals("String", user.getId().getClass().getSimpleName())
        );
    }

    @Test
    void test_userCanRegister(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com", "Jesus123");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        assertTrue(userService.getUserDatabase().contains(user));
        assertEquals(userService.getUserDatabase().size(), 2);
    }

    @Test
    void test_userCanFindOtherUsers(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        User user3 = userService.registerNative("Eseosa", "Nathan", "eseosaedemakhiota12@gmail.com", "Jesus123");

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
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User user2 = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        assertThrows(UserException.class, ()-> userService.find("Ehis"));
    }

    @Test
    void test_ThatUserCannotSendRequestToTheSameUserTwice(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");

        userService.sendFriendRequest(sender.getId(), receiver.getId());
        assertThatThrownBy(()-> userService.sendFriendRequest(sender.getId(), receiver.getId())).isInstanceOf(FriendRequestException.class);
    }

    @AfterEach
    void tearDown(){
        userService.getUserDatabase().deleteAll();
        userDatabase.getEmails().clear();
    }
}