package models;
import java.util.Arrays;

public class Native extends User {

    public Native(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
    @Override
    public String getNativeId() {
        return this.getId();
    }

    @Override
    public void updateInbox(Message chatMessage) {
        inbox.put(chatMessage.getSenderId(), Arrays.asList(chatMessage));
    }

    @Override
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
