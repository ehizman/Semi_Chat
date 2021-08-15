package models;

import lombok.Getter;
import repository.Storable;
import services.UserService;

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
    private final List<Message> friendRequests = new ArrayList<>();
    @Getter
    private final List<String> friendList = new ArrayList<>();

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
}
