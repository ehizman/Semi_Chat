package models;

import lombok.Data;
import lombok.Getter;
import repository.Storable;

import java.util.*;

@Data
public abstract class User implements Storable {
    private List<Message> chatRoomInbox = new ArrayList<>();
    private String firstName;
    private String lastName;
    private String email;
    private final List<FriendRequest> friendRequests = new ArrayList<>();
    private final List<String> friendList = new ArrayList<>();
    private final String password;
    private boolean isLoggedIn;
    private final String id  = UUID.randomUUID().toString();
    @Getter
    private String profile;
    Map<String, List<Message>> inbox = new HashMap<>();
    Map<String, List<Message>> sentMessages = new HashMap<>();
    List<String> chatRooms = new ArrayList<>();

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

    public void updatePendingFriendRequests(FriendRequest message){
        this.friendRequests.add(message);
    }
//    public String readMessage(int messageIndex){
//        return friendRequests.get(messageIndex).toString();
//    }

    public abstract String getNativeId();

    public abstract void updateInbox(Message chatMessage);

    public Map<String, List<Message>> getSentMessages(){
        return sentMessages;
    }
}
