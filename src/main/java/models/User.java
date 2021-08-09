package models;

import lombok.Getter;
import repository.Storable;

import java.util.UUID;

public abstract class User implements Storable {
    private final String id = UUID.randomUUID().toString();
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void updateUserFirstName(String name){
        this.firstName = name;
    }

    public void updateUserLastName(String name){
        this.lastName = name;
    }

    public void updateUserEmail(String email){
        this.email = email;
    }
}
