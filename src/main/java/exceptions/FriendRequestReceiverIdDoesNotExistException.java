package exceptions;

public class FriendRequestReceiverIdDoesNotExistException extends RuntimeException {
    public FriendRequestReceiverIdDoesNotExistException(String message) {
        super(message);
    }
}
