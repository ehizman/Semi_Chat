package services;

import lombok.Getter;
import models.Native;
import models.User;
import repository.Database;
import repository.UserDatabaseImpl;

public class RegisterService {
    @Getter
    private final Database<User> userDatabase;

    private RegisterService(){
        this.userDatabase = new UserDatabaseImpl<>();
    }
    public  User registerNative(String firstName, String lastName, String email) {
        User user = new Native(firstName, lastName, email);
        userDatabase.save(user);
        return user;
    }

    public static class RegisterSingletonHelper{
        private static final RegisterService instance = new RegisterService();
    }

    public static RegisterService createSingletonOfRegisterService(){
        return RegisterSingletonHelper.instance;
    }
}
