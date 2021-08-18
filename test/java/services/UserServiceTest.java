package services;

import exceptions.UserException;
import exceptions.UserLoginException;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserDatabaseImpl;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDatabaseImpl<User> userDatabase;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp(){
        userService = UserService.getInstance();
        userDatabase = (UserDatabaseImpl<User>) UserDatabaseImpl.getInstance();
    }

    @Test
    void test_ThatUserCanLogin(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        userService = UserService.getInstance();
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
        User user = userService.registerNative(firstName, lastName, email, password);
        assertThatThrownBy(()-> userService.login(email, "Jesu123")).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_UserCanLogout(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        userService = UserService.getInstance();
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
        userService = UserService.getInstance();
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
        userService = UserService.getInstance();
        User user = userService.registerNative(firstName, lastName, password, email);
        userService.logout(user);
        assertThatThrownBy(()-> userService.logout(user)).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_ThatUsersHaveUniqueEmail(){
        userService = UserService.getInstance();
        userService.registerNative("Ehis", "Edemakhiota",
                "edemaehiz@gmail.com", "EdemaEhi17.");
        assertThatThrownBy(()-> userService.registerNative("Eesosa", "Ehigie", "edemaehiz@gmail.com",
                "edemakhiota17.")).isInstanceOf(UserException.class);
    }

    @AfterEach
    void tearDown() {
        userDatabase.getEmails().clear();
        userService.getUserDatabase().deleteAll();
    }
}