package services;

import exceptions.FriendRequestException;
import exceptions.UserDoesNotExistException;
import lombok.Getter;
import models.Message;
import models.Native;
import models.RequestStatus;
import models.User;
import repository.Database;
import repository.UserDatabaseImpl;

import java.time.LocalDateTime;
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
        Optional<List<User>> users =  userDatabase.findAllByName(namePattern);
        if (users.isEmpty()){
            throw new UserDoesNotExistException("No user found!");
        }
        return users.get();
    }

    public void sendFriendRequest(String senderId, String receiverId) {
        Optional<User> optionalSender = userDatabase.findById(senderId);
        if (optionalSender.isPresent()){
            String senderName = optionalSender.get().getName();
            RequestObject requestObject = new RequestObject(senderName, senderId, receiverId);
            Optional<User> optionalReceiver = userDatabase.findById(receiverId);
            if (optionalReceiver.isPresent()){
                sendFriendRequest(requestObject, optionalReceiver.get());
            }
            else{
                throw new FriendRequestException("Friend request receiver id does not exist");
            }
        }
        else{
            throw new FriendRequestException("Friend request sender does not exist");
        }
    }

    private void sendFriendRequest(RequestObject requestObject, User receiver) {
        FriendRequestDispatcher friendRequestDispatcher = new FriendRequestDispatcher();
        friendRequestDispatcher.send(receiver, requestObject);
    }

    public void friendMatcher(Message<RequestObject> requestObject) {
        User sender = userDatabase.findById(requestObject.getSenderId()).get();
        sender.getFriendList().add(requestObject.getReceiverId());
    }

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }

    private final static class RequestObject implements Message {
        private final String senderName;
        @Getter
        private final String senderId;
        @Getter
        private final String receiverId;
        private final LocalDateTime timeSent;
        @Getter
        private final RequestStatus requestStatus = RequestStatus.PENDING;
        
        private RequestObject(String senderName, String senderId, String receiverId){
            this.senderName = senderName;
            this.timeSent = LocalDateTime.now();
            this.senderId = senderId;
            this.receiverId = receiverId;
        }

        public String toString(){
            int year = timeSent.getYear();
            int month = timeSent.getMonthValue();
            int day = timeSent.getDayOfMonth();
            int hour = timeSent.getHour();
            int minute = timeSent.getMinute();
            return String.format("You have received a friend request from %s at %s", senderName, String.format("%d-%d-%d:%02d:%02d",year, month, day, hour, minute));
        }
    }
}
