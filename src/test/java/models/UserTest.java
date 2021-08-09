package models;

import org.junit.jupiter.api.Test;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void test_constructor(){
        User user = new Native("Ehis", "Edemakhiota", "ehizman@gmail.com");
        assertAll(
                ()-> assertEquals("Ehis", user.getFirstName()),
                () -> assertEquals("Edemakhiota", user.getLastName()),
                () -> assertEquals("ehizman@gmail.com", user.getEmail())
        );
    }

    @Test
    void test_user_can_register(){
        RegisterService registerService = RegisterService.createSingletonOfRegisterService();
        User user = registerService.registerNative("Ehis", "Edemakhiota", "edemaehiz@gmail.com");
        User user2 = registerService.registerNative("Eseosa", "Edemakhiota", "eseosaedemakhiota@gmail.com");
        assertTrue(registerService.getUserDatabase().contains(user));
        assertEquals(registerService.getUserDatabase().size(), 2);
    }

}