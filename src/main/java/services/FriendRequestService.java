package services;

import exceptions.UserException;
import models.FriendRequest;
import models.Message;
import models.User;

public class FriendRequestService {

    public void createFriendRequestAndDispatch(User sender, User receiver) {
        FriendRequest friendRequest = new FriendRequest(sender.getName(), sender.getId(), receiver.getId());
        dispatchFriendRequests(receiver, friendRequest);
    }

    public void dispatchFriendRequests(User receiver, Message friendRequest) {
        receiver.updatePendingFriendRequests((FriendRequest) friendRequest);
    }
    public void acceptFriendRequests(User friendRequestSender, User friendRequestRecipient, FriendRequest  friendRequest) {
        friendRequestRecipient.getFriendList().add(friendRequestSender.getId());
        friendRequestSender.getFriendList().add(friendRequestRecipient.getId());
        friendRequestRecipient.getFriendRequests().remove(friendRequest);
    }
}
