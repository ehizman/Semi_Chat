package exceptions;

public class FriendRequestSenderIdDoesNotExistException extends RuntimeException {
    public FriendRequestSenderIdDoesNotExistException(String message) {
        super(message);
    }
}
