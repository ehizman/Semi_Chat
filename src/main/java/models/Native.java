package models;

import exceptions.UserLoginException;
import services.UserService;

import java.util.UUID;

public class Native extends User {
    private final String id  = UUID.randomUUID().toString();
    private UserService userService = UserService.getInstance();

    public Native(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    @Override
    public void login(String email, String password) {
        if(userService.isValidLogin(email, password)){
            this.setLoggedIn(true);
        }
    }

    @Override
    public void logout() {
        if (!isLoggedIn()){
            throw new UserLoginException("You are already logged out");
        }
        this.setLoggedIn(false);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
