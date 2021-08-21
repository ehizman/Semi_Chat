package services;

import dto.RegisterDto;
import exceptions.FriendRequestException;
import exceptions.UnSupportedActionException;
import exceptions.UserException;
import exceptions.UserLoginException;
import lombok.Getter;
import models.*;
import repository.Database;
import repository.DatabaseImpl;

import java.util.List;

public class UserService {
    @Getter
    private final Database<User> userDatabase;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;
    private final ChatroomService chatroomService;

    @SuppressWarnings("unchecked")
    private UserService(){
        this.userDatabase =(Database<User>) DatabaseImpl.getInstance();
        this.messageService = new MessageService();
        this.friendRequestService = new FriendRequestService();
        this.chatroomService = new ChatroomService();
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
        User sender = checkIfUserExistsInDataBaseByIdElseThrowException(senderId, "Friend request " +
                "sender does not exist");

        User receiver = checkIfUserExistsInDataBaseByIdElseThrowException(receiverId, "Friend request receiver " +
                "id does not exist");

        if (sender.getFriendList().contains(receiverId)){
            throw new FriendRequestException("Receiver is already a friend");
        }

        for (Message friendRequest: receiver.getFriendRequests()) {
            if (friendRequest.getReceiverId().equals(receiverId)) {
                throw new FriendRequestException("Friend request has been sent already!");
            }
        }
        friendRequestService.createFriendRequestAndDispatch(sender, receiver);
    }

    private User checkIfUserExistsInDataBaseByIdElseThrowException(String senderId, String exceptionMessage) {
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
        User sender = checkIfUserExistsInDataBaseByIdElseThrowException(senderId, "Sender does not exist");
        User receiver = checkIfUserExistsInDataBaseByIdElseThrowException(receiverId, "Message receiver does not exist");

        if (!receiver.getFriendList().contains(senderId)){
            throw new UnSupportedActionException("Cannot dispatchFriendRequests message to someone who isn't a friend");
        }
        messageService.createChatMessageAndDispatch(sender, receiver,messageBody);
    }

    void acceptFriendRequest(FriendRequest friendRequest){
        User friendRequestRecipient = checkIfUserExistsInDataBaseByIdElseThrowException(friendRequest.getReceiverId()
                ,"No friend Request receiver found!");
        User friendRequestSender = checkIfUserExistsInDataBaseByIdElseThrowException(friendRequest.getSenderId() ,"No" +
                " friend Request receiver found!");
        friendRequestService.acceptFriendRequests(friendRequestSender, friendRequestRecipient, friendRequest);
    }

    void rejectFriendRequest(FriendRequest requestObject){
        User user = checkIfUserExistsInDataBaseByIdElseThrowException(requestObject.getReceiverId(),
                "No friend Request receiver found!");
        user.getFriendRequests().remove(requestObject);
    }

    public String createChatRoom(String adminId, String groupName, String...memberIds){
       String chatRoomId =  chatroomService.createNewChatRoom(adminId, groupName, memberIds);
       User admin = checkIfUserExistsInDataBaseByIdElseThrowException(adminId, "User does not exist!");
       admin.getChatRooms().add(chatRoomId);
       for(String id : memberIds){
           User user = checkIfUserExistsInDataBaseByIdElseThrowException(id,"User does not exist");
           user.getChatRooms().add(chatRoomId);
       }
       return chatRoomId;
    }

    public ChatMessage broadcastMessage(String chatRoomId, String senderId, String message) {
        User sender = checkIfUserExistsInDataBaseByIdElseThrowException(senderId, "User does not exist in database");
        if(!sender.getChatRooms().contains(chatRoomId)){
            throw new UnSupportedActionException("You are no longer a part of this group!");
        }
        ChatMessage chatMessage = messageService.createNewChatMessage(sender.getName(), senderId, chatRoomId, message);
        chatroomService.broadcast(chatRoomId,chatMessage);
        return chatMessage;
    }

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }
}
