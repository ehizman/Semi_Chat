package services;

import dto.RegisterDto;
import exceptions.FriendRequestException;
import exceptions.UserException;
import exceptions.UserLoginException;
import lombok.Getter;
import models.Message;
import models.Native;
import models.RequestStatus;
import models.User;
import repository.Database;
import repository.UserDatabaseImpl;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    @Getter
    private final Database<User> userDatabase;

    @SuppressWarnings("unchecked")
    private UserService(){
        this.userDatabase = (Database<User>) UserDatabaseImpl.getInstance();
    }

    public  User registerNative(String firstName, String lastName, String email, String password) {
        userDatabase.checkEmail(email);
        userDatabase.addNewEmail(email);

        RegisterDto registerDto = prettify(firstName, lastName, email,password);
        User user = new Native(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(),
                registerDto.getPassword());
        userDatabase.save(user);
        return user;
    }

    private RegisterDto prettify(String firstName, String lastName, String email, String password) {
        firstName = firstName.toUpperCase();
        lastName = lastName.toUpperCase();
        email = email.toLowerCase();
        RegisterDto registerDto = new RegisterDto(firstName, lastName, email, password);
        return registerDto;
    }

    public List<User> find(String namePattern) {
        return userDatabase.findAllByName(namePattern).orElseThrow(()-> new UserException("No user found!"));
    }

    public void sendFriendRequest(String senderId, String receiverId) {
        User sender = userDatabase.findById(senderId).orElseThrow(()-> new FriendRequestException("Friend request " +
                "sender does not exist"));

        String senderName = sender.getName();

        User receiver = userDatabase.findById(receiverId).orElseThrow(()-> new FriendRequestException("Friend request receiver id does not exist"));

        RequestObject requestObject = new RequestObject(senderName, senderId, receiverId);

        sendFriendRequest(requestObject, receiver);
    }

    private void sendFriendRequest(RequestObject requestObject, User receiver) {
        FriendRequestDispatcher friendRequestDispatcher = new FriendRequestDispatcher();
        friendRequestDispatcher.send(receiver, requestObject);
    }

    public void matchFriends(Message<RequestObject> requestObject) {
        User sender = userDatabase.findById(requestObject.getSenderId()).orElseThrow(()-> new UserException(
                "sender does not exist"));
        sender.getFriendList().add(requestObject.getReceiverId());
    }

    public boolean isValidLogin(String email, String password) {
        User user = userDatabase.findByEmail(email).orElseThrow(()-> new UserLoginException("Invalid details"));
        if (user.getPassword().equals(password)){
            return true;
        }
        else{
            throw new UserLoginException("Invalid login details");
        }
    }

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }

    private final static class RequestObject implements Message<RequestObject>{
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
