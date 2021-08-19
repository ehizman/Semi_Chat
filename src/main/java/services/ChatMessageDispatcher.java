package services;

import models.Message;
import models.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ChatMessageDispatcher implements MessageDispatcher {
    @Override
    public void send(User receiver, Message chatMessage) {
        System.out.println("Sender id : " + chatMessage.getSenderId());
        String senderId = chatMessage.getSenderId();
        Map<String, List<Message>> receiverInbox = receiver.getInbox();
        if (receiverInbox.containsKey(senderId)){
            receiverInbox.get(senderId).add(chatMessage);
        }
        else{
            receiverInbox.put(senderId, Arrays.asList(chatMessage));
        }
    }
}
