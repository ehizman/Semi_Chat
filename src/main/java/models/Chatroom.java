package models;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Chatroom implements Observable{
    @Getter
    Set<String> members;
    @Getter
    String groupId = UUID.randomUUID().toString();

    public Chatroom(){
        this.members = new HashSet<>();
    }
    @Override
    public void register(String userId) {
        members.add(userId);
    }

    @Override
    public void removeObserver(String userId) {
        members.remove(userId);
    }

    @Override
    public void broadcast(Message<ChatMessage> chatMessage) {

    }
}
