package services;

import exceptions.UserDoesNotExistException;
import lombok.Getter;
import models.Native;
import models.User;
import repository.Database;
import repository.UserDatabaseImpl;

import java.util.List;
import java.util.Optional;

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

    public List<User> find(String namePattern) {
        Optional<List<User>> users =  userDatabase.findAllByUserName(namePattern);
        if (users.isEmpty()){
            throw new UserDoesNotExistException("No user found!");
        }
        return users.get();
    }

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }
}
