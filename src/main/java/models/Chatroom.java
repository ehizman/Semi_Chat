package models;

import exceptions.ChatroomException;
import lombok.Data;
import lombok.Getter;

import java.util.*;

public class Chatroom implements Observable{
    @Getter
    Set<String> members;
    @Getter
    String groupId = UUID.randomUUID().toString();
    private final Set<String> admins;
    private String groupName;

    public Chatroom(String adminId, String groupName, String... members){
        this.members = new HashSet<>();
        this.members.addAll(Arrays.asList(members));
        this.admins = new HashSet<>();
        this.admins.add(adminId);
        this.groupName = groupName;
    }

    @Override
    public void subscribe(String... userIds) {
        members.addAll(List.of(userIds));
    }

    @Override
    public void removeObserver(String userId) {
        members.remove(userId);
    }

    @Override
    public void broadcast(Message chatMessage) {

    }

    public void addAdmin(String id){
        if (admins.size() == 3){
            throw new ChatroomException("Cannot add more than 3 admins");
        }
        admins.add(id);
    }

    public void removeAdmin(String id){
        if (admins.size() == 1){
            throw new ChatroomException("Chatroom must have at least one Admin");
        }
        admins.remove(id);
    }
}
