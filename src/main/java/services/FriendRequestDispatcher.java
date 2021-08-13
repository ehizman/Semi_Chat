package services;

import models.Message;
import models.User;

public class FriendRequestDispatcher implements MessageDispatcher{
    @Override
    public void send(User receiver, Message friendRequest) {
        receiver.updatePendingFriendRequests(friendRequest);
    }
}
