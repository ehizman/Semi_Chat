package services;

import exceptions.ChatroomException;
import models.Chatroom;
import models.Message;
import models.User;
import repository.Database;
import repository.DatabaseImpl;

import java.util.List;

public class ChatroomService {
    private final Database<User> userDatabase = (Database<User>) DatabaseImpl.getInstance();
    public String createNewChatRoom(String adminId, String groupName, String... memberIds) {
        Chatroom chatroom = new Chatroom(adminId, groupName, memberIds);
        return chatroom.getGroupId();
    }

    public void broadcast(String chatRoomId, Message chatMessage) {
        List<User> listOfGroupMembers = userDatabase.findAllMembersThatBelongToGroupWithThisId(chatRoomId).
                orElseThrow(()-> new ChatroomException("No users found in chatroom"));
        for (User user: listOfGroupMembers) {
            if (user.getChatRooms().contains(chatRoomId)){
                user.getChatRoomInbox().add(chatMessage);
            }
        }
    }
}
