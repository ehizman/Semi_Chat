package services;

import exceptions.UserException;
import models.FriendRequest;
import models.Message;
import models.User;
import repository.UserDatabaseImpl;

public class Util {
    private static final UserDatabaseImpl<User> userDatabase = (UserDatabaseImpl<User>) UserDatabaseImpl.getInstance();
    public static void matchFriends(Message<FriendRequest> requestObject) {
        User sender = userDatabase.findById(requestObject.getSenderId()).orElseThrow(()-> new UserException(
                "sender does not exist"));
        sender.getFriendList().add(requestObject.getReceiverId());
    }
}
