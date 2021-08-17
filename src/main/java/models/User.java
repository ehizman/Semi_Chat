package models;

import lombok.Data;
import lombok.Getter;
import repository.Storable;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class User implements Storable {
    private String firstName;
    private String lastName;
    private String email;
    private final List<Message> friendRequests = new ArrayList<>();
    private final List<String> friendList = new ArrayList<>();
    private final String password;
    private boolean isLoggedIn;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = true;
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

    public void updatePendingFriendRequests(Message message){
        this.friendRequests.add(message);
    }
    public String readMessage(int messageIndex){
        return friendRequests.get(messageIndex).toString();
    }

    void handleRequests(Message requestObject, RequestStatus requestStatus){
        if (requestStatus == RequestStatus.ACCEPTED) {
            addSenderToFriendListAndUpdateSenderFriendList(requestObject);
        }
        else{
            if (requestStatus == RequestStatus.REJECTED){
                friendList.remove(requestObject);
            }
        }
    }

    private void addSenderToFriendListAndUpdateSenderFriendList(Message requestObject) {
        UserService userService = UserService.getInstance();
        friendList.add(requestObject.getSenderId());
        userService.matchFriends(requestObject);
    }

    public abstract void login(String email, String password);

    public abstract void logout();
}
