package repository;

import models.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseImplTest {
    @Test
    @SuppressWarnings("unchecked")
    void test_Singleton(){
        DatabaseImpl<User> userDatabase = (DatabaseImpl<User>) DatabaseImpl.getInstance();
        DatabaseImpl<User> userDatabase1 = (DatabaseImpl<User>) DatabaseImpl.getInstance();
        assertThat(userDatabase).isEqualTo(userDatabase1);
    }

}