package models;

import exceptions.UserLoginException;
import services.UserService;

import java.util.List;

public class Native extends User {
    private UserService userService = UserService.getInstance();


    public Native(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    @Override
    public List<User> search(String namePattern) {
        List<User> usersThatMatchNamePattern =userService.find(namePattern);
        usersThatMatchNamePattern.forEach(user -> System.out.println(user.getProfile()));
        return usersThatMatchNamePattern;
    }

    @Override
    public String getNativeId() {
        return this.getId();
    }

    @Override
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
