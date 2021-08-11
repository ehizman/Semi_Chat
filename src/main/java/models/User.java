package models;

import lombok.Getter;
import repository.Storable;

import java.util.ArrayList;
import java.util.List;

public abstract class User implements Storable {
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    @Getter
    private List<Message> inbox = new ArrayList<>();

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

    public void updateInbox(Message message){
        this.inbox.add(message);
    }
    public String readMessage(int messageIndex){
        return inbox.get(messageIndex).toString();
    }
}
