package services;

import exceptions.FriendRequestException;
import exceptions.UnSupportedActionException;
import exceptions.UserException;
import exceptions.UserLoginException;
import models.Chatroom;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private DatabaseImpl<User> userDatabase;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp(){
        userService = UserService.getInstance();
        userDatabase = (DatabaseImpl<User>) DatabaseImpl.getInstance();
    }

    @Test
    void test_ThatUserCanLogin(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        User user = userService.registerNative(firstName, lastName, email, password);
        assertThat(user.isLoggedIn()).isEqualTo(true);
    }

    @Test
    void test_ThatThrowsUserLoginExceptionWhenInvalidDetailsAreProvided(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        userService = UserService.getInstance();
        userService.registerNative(firstName, lastName, email, password);
        assertThatThrownBy(()-> userService.login(email, "Jesu123")).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_UserCanLogout(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        User user = userService.registerNative(firstName, lastName, email, password);
        userService.logout(user);
        assertThat(user.isLoggedIn()).isEqualTo(false);
    }

    @Test
    void test_UserCanLoginAfterLoggingOut(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        User user = userService.registerNative(firstName, lastName, email, password);
        userService.logout(user);
        userService.login("edemaehiotaeseosa@gmail.com", "Jesus123");
        assertThat(user.isLoggedIn()).isEqualTo(true);
    }

    @Test
    void testThatUserCannotLogoutWhenAlreadyLoggedOut(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        User user = userService.registerNative(firstName, lastName, password, email);
        userService.logout(user);
        assertThatThrownBy(()-> userService.logout(user)).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_ThatUsersHaveUniqueEmail(){
        userService.registerNative("Ehis", "Edemakhiota",
                "edemaehiz@gmail.com", "EdemaEhi17.");
        assertThatThrownBy(()-> userService.registerNative("Eesosa", "Ehigie", "edemaehiz@gmail.com",
                "edemakhiota17.")).isInstanceOf(UserException.class);
    }

    @Test
    void test_ThatUserCanSendMessages(){
        User sender = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com", "EdemaEhi17.");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "edemaehiota@gmail.com",
                "eseosaEdemakhiota");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        userService.acceptFriendRequest(receiver.getFriendRequests().get(0));
        userService.sendChatMessage(sender.getId(), receiver.getId(), "Hello there");
        assertThat(receiver.getInbox()).hasSize(1);
        assertThat(receiver.getInbox().containsKey(sender.getId())).isTrue();
        assertThat(receiver.getInbox().get(sender.getId()).
                contains(sender.getSentMessages().get(receiver.getId()).get(0))).isTrue();
    }

    @Test
    void test_ThatUserCanSendMessageToOnlyUsersInTheirFriendList(){
        User sender = userService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com", "EdemaEhi17.");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "edemaehiota@gmail.com",
                "eseosaEdemakhiota");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        assertThatThrownBy(()->userService.sendChatMessage(sender.getId(), receiver.getId(), "Hello there"))
                .isInstanceOf(UnSupportedActionException.class);
    }

    @Test
    void test_ThatUserCanFindAnotherUserBySupplyingANamePattern(){
        User firstUser = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User secondUser = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com",
                "Jesus123");
        List<User> usersThatMatchNamePattern = userService.find("Eseosa");
        assertThat(usersThatMatchNamePattern).hasSameElementsAs(Arrays.asList(firstUser, secondUser));
    }

    @Test
    void test_UserCanSendFriendRequests(){
        User user = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(user.getId(), receiver.getId());
        assertThat(receiver.getFriendRequests().size()).isEqualTo(1);
    }

    @Test
    void test_ThatUserCanAcceptFriendRequestsAndAddFriends(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        userService.acceptFriendRequest(receiver.getFriendRequests().get(0));
        assertThat(receiver.getId()).isIn(sender.getFriendList());
        assertThat(sender.getId()).isIn(receiver.getFriendList());
    }

    @Test
    void test_ThatUserCanRejectFriendRequest(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        userService.sendFriendRequest(sender.getId(), receiver.getId());
        userService.rejectFriendRequest(receiver.getFriendRequests().get(0));
        assertThat(receiver.getFriendList()).doesNotContain(sender.getId());
        assertThat(receiver.getFriendList()).isEmpty();
    }

    @Test
    void test_ThatUserCannotSendRequestsToSomeoneWhoIsAlreadyAFriend(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");

        userService.sendFriendRequest(sender.getId(), receiver.getId());
        userService.acceptFriendRequest(receiver.getFriendRequests().get(0));
        assertThatThrownBy(()-> userService.sendFriendRequest(sender.getId(), receiver.getId())).isInstanceOf(FriendRequestException.class);
    }

    @Test
    void test_ThatUserCanCreateChatRoom(){
        User sender = userService.registerNative("Eseosa", "Magul", "ehizman@gmail.com", "Jesus123");
        User receiver = userService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com", "Jesus123");
        String adminId = sender.getId();
        String firstMemberId = receiver.getId();
        Chatroom chatroom = userService.createChatRoom(adminId, firstMemberId);
        assertThat(chatroom.getMembers().contains(firstMemberId)).isTrue();
    }

    @AfterEach
    void tearDown() {
        userDatabase.getEmails().clear();
        userService.getUserDatabase().deleteAll();
    }
}