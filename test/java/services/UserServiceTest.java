package services;

import exceptions.UserException;
import exceptions.UserLoginException;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserDatabaseImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(()-> user.login(email, "Jesu123")).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_UserCanLogout(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        userService = UserService.getInstance();
        User user = userService.registerNative(firstName, lastName, email, password);
        user.logout();
        assertThat(user.isLoggedIn()).isEqualTo(false);
    }

    @Test
    void testThatUserCannotLogoutWhenAlreadyLoggedOut(){
        String firstName = "Eseosa";
        String lastName = "Edemakhiota";
        String email = "edemaehiotaeseosa@gmail.com";
        String password = "Jesus123";
        userService = UserService.getInstance();
        User user = userService.registerNative(firstName, lastName, password, email);
        user.logout();
        assertThatThrownBy(user::logout).isInstanceOf(UserLoginException.class);
    }

    @Test
    void test_ThatUserSHaveUniqueEmail(){
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