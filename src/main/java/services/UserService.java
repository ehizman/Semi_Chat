package services;

import lombok.Getter;
import models.Native;
import models.User;
import repository.Database;
import repository.UserDatabaseImpl;

public class UserService {
    @Getter
    private final Database<User> userDatabase;

    private UserService(){
        this.userDatabase = new UserDatabaseImpl<>();
    }
    public  User registerNative(String firstName, String lastName, String email) {
        User user = new Native(firstName, lastName, email);
        userDatabase.save(user);
        return user;
    }

    public static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService createSingletonOfUserService(){
        return UserServiceSingletonHelper.instance;
    }
}
