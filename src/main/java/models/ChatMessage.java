package models;

public class ChatMessage implements Message<ChatMessage>{
    @Override
    public String getSenderId() {
        return null;
    }

    @Override
    public String getReceiverId() {
        return null;
    }
}
