package services;

import dto.RegisterDto;
import exceptions.FriendRequestException;
import exceptions.UserException;
import exceptions.UserLoginException;
import lombok.Getter;
import models.*;
import repository.Database;
import repository.UserDatabaseImpl;

import java.util.List;

public class UserService {
    @Getter
    private final Database<User> userDatabase;

    private UserService(){
        this.userDatabase =(Database<User>) UserDatabaseImpl.getInstance();
    }

    public  User registerNative(String firstName, String lastName, String email, String password) {
        userDatabase.checkEmail(email);
        userDatabase.addNewEmail(email);
        System.out.println(firstName + "" +lastName + " " +email);
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
        User sender = userDatabase.findById(senderId).orElseThrow(()-> new FriendRequestException("Friend request " +
                "sender does not exist"));

        String senderName = sender.getName();

        User receiver = userDatabase.findById(receiverId).orElseThrow(()-> new FriendRequestException("Friend request receiver id does not exist"));

        FriendRequest requestObject = new FriendRequest(senderName, senderId, receiverId);

        sendFriendRequest(requestObject, receiver);
    }

    private void sendFriendRequest(FriendRequest requestObject, User receiver) {
        FriendRequestDispatcher friendRequestDispatcher = new FriendRequestDispatcher();
        friendRequestDispatcher.send(receiver, requestObject);
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

    private static class UserServiceSingletonHelper {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance(){
        return UserServiceSingletonHelper.instance;
    }
}
