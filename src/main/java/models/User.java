package models;

import lombok.Data;
import lombok.Getter;
import repository.Storable;
import services.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public abstract class User implements Storable {
    private String firstName;
    private String lastName;
    private String email;
    private final List<Message<FriendRequest>> friendRequests = new ArrayList<>();
    private final List<String> friendList = new ArrayList<>();
    private final String password;
    private boolean isLoggedIn;
    private final String id  = UUID.randomUUID().toString();
    @Getter
    private String profile;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = true;
        this.profile = String.format("""
            First name: %s
            Last name: %s
            Email: %s
            id : %s""", getFirstName(), getLastName(), getEmail(), getId());
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

    public void updatePendingFriendRequests(Message<FriendRequest> message){
        this.friendRequests.add(message);
    }
    public String readMessage(int messageIndex){
        return friendRequests.get(messageIndex).toString();
    }

    void handleRequests(Message<FriendRequest> requestObject, RequestStatus requestStatus){
        if (requestStatus == RequestStatus.ACCEPTED) {
            friendList.add(requestObject.getSenderId());
            Util.matchFriends(requestObject);
        }
        else{
            if (requestStatus == RequestStatus.REJECTED){
                friendRequests.remove(requestObject);
            }
        }
    }

    public abstract List<User> search(String namePattern);

    public abstract String getNativeId();
}
