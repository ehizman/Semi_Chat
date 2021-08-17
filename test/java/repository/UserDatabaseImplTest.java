package repository;

import models.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseImplTest {
    @Test
    @SuppressWarnings("unchecked")
    void test_Singleton(){
        UserDatabaseImpl<User> userDatabase = (UserDatabaseImpl<User>) UserDatabaseImpl.getInstance();
        UserDatabaseImpl<User> userDatabase1 = (UserDatabaseImpl<User>) UserDatabaseImpl.getInstance();
        assertThat(userDatabase).isEqualTo(userDatabase1);
    }

}