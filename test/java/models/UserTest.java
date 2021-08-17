package models;

import exceptions.UserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private UserService userService;
    @BeforeEach
    void setUp(){
        userService = UserService.getInstance();
    }
    @Test
    void test_Constructor(){
        User user = userService.registerNative("Ehis", "Edemakhiota", "ehizman@gmail.com", "Jesus123");
        assertAll(
                ()-> assertEquals("Ehis", user.getFirstName()),
                () -> assertEquals("Edemakhiota", user.getLastName()),
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
        User user3 = userService.registerNative("Eseosa", "Nathan", "eseosaedemakhiota@gmail.com", "Jesus123");

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
    void test_UserCanSendFriendRequests(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(user.getId(), receiver.getId());
        assertThat(receiver.getFriendRequests().size()).isEqualTo(1);
        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();
        int day = LocalDateTime.now().getDayOfMonth();
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        String formattedTime = String.format("%d-%d-%d:%02d:%02d",year, month, day, hour, minute);
        assertThat(receiver.readMessage(0)).contains("You have received a friend request from Eseosa " +
                        "Magul at "+ formattedTime);
    }

    @Test
    void test_ThatUserCanAcceptFriendRequestsAndAddFriends(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        receiver.handleRequests(receiver.getFriendRequests().get(0), RequestStatus.ACCEPTED);
        assertThat(receiver.getId()).isIn(sender.getFriendList());
        assertThat(sender.getId()).isIn(receiver.getFriendList());
    }

    @Test
    void test_ThatUserCanRejectFriendRequest(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        receiver.handleRequests(receiver.getFriendRequests().get(0), RequestStatus.REJECTED);
        assertThat(receiver.getFriendList()).doesNotContain(sender.getId());
        assertThat(receiver.getFriendList()).isEmpty();
    }

    @AfterEach
    void tearDown(){
        userService.getUserDatabase().deleteAll();
    }
}