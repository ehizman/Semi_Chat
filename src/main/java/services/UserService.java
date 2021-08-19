package services;

import dto.RegisterDto;
import exceptions.FriendRequestException;
import exceptions.UnSupportedActionException;
import exceptions.UserException;
import exceptions.UserLoginException;
import lombok.Getter;
import models.*;
import repository.Database;
import repository.UserDatabaseImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserService {
    @Getter
    private final Database<User> userDatabase;

    @SuppressWarnings("unchecked")
    private UserService(){
        this.userDatabase =(Database<User>) UserDatabaseImpl.getInstance();
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
        return new RegisterDto(firstName, lastName, email, password);
    }

    public void logout(User user) {
        if (!user.isLoggedIn()){
            throw new UserLoginException("You are already logged out");
        }
        user.setLoggedIn(false);
    }

    public List<User> find(String namePattern) {
        return userDatabase.findAllByName(namePattern).orElseThrow(()-> new UserException("No user found!"));
    }


    public void sendFriendRequest(String senderId, String receiverId) {
        User sender = checkIfUserExistsInDataBaseElseThrowException(senderId, "Friend request " +
                "sender does not exist");

        User receiver = checkIfUserExistsInDataBaseElseThrowException(receiverId, "Friend request receiver id does not exist");


        if (sender.getFriendList().contains(receiverId)){
            throw new FriendRequestException("Receiver is already a friend");
        }

        for (Message friendRequest: receiver.getFriendRequests()) {
            if (friendRequest.getReceiverId().equals(receiverId)) {
                throw new FriendRequestException("Friend request has been sent already!");
            }
        }

        String senderName = sender.getName();

        FriendRequest requestObject = new FriendRequest(senderName, senderId, receiverId);

        FriendRequestDispatcher friendRequestDispatcher = new FriendRequestDispatcher();
        friendRequestDispatcher.send(receiver, requestObject);
    }

    private User checkIfUserExistsInDataBaseElseThrowException(String senderId, String exceptionMessage) {
        return userDatabase.findById(senderId).orElseThrow(() -> new FriendRequestException(exceptionMessage));
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

    public void login(String email, String password) {
        if(isValidLogin(email, password)){
            User user = userDatabase.findByEmail(email).orElseThrow(()->new UserLoginException("No record found"));
            user.setLoggedIn(true);
        }
    }

    public void sendChatMessage(String senderId, String receiverId, String messageBody) {
        User sender = checkIfUserExistsInDataBaseElseThrowException(senderId, "Sender does not exist");
        User receiver = checkIfUserExistsInDataBaseElseThrowException(receiverId, "Message receiver does not exist");

        if (!receiver.getFriendList().contains(senderId)){
            throw new UnSupportedActionException("Cannot send message to someone who isn't a friend");
        }
        Message chatMessage = new ChatMessage(sender.getName(), senderId, receiverId,messageBody);
        updateSenderSentMessages(sender, receiverId, chatMessage);
        MessageDispatcher chatMessageDispatcher = new ChatMessageDispatcher();
        chatMessageDispatcher.send(receiver, chatMessage);
    }

    private void updateSenderSentMessages(User sender, String receiverId, Message chatMessage) {
        Map<String, List<Message>> senderSentMessages = sender.getSentMessages();
        if (senderSentMessages.containsKey(receiverId)){
            senderSentMessages.get(receiverId).add(chatMessage);
        }
        else {
            senderSentMessages.put(receiverId, Arrays.asList(chatMessage));
        }
    }

    void acceptFriendRequest(FriendRequest requestObject){
       User user = userDatabase.findById(requestObject.getReceiverId()).orElseThrow(()->
               new UserException("No friend Request receiver found!"));
       user.getFriendList().add(requestObject.getSenderId());
        matchFriends(requestObject);
        user.getFriendRequests().remove(requestObject);
    }
    void rejectFriendRequest(FriendRequest requestObject){
        User user = userDatabase.findById(requestObject.getReceiverId()).orElseThrow(()->
                new UserException("No friend Request receiver found!"));
        user.getFriendRequests().remove(requestObject);
    }
    public void matchFriends(FriendRequest requestObject) {
        User sender = userDatabase.findById(requestObject.getSenderId()).orElseThrow(()-> new UserException(
                "sender does not exist"));
        sender.getFriendList().add(requestObject.getReceiverId());
    }

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }
}
