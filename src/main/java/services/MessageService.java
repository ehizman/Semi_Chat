package services;
import models.ChatMessage;
import models.Message;
import models.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MessageService {
    public void createChatMessageAndDispatch(User sender, User receiver, String messageBody) {
        ChatMessage chatMessage = new  ChatMessage(sender.getName(), sender.getId(), receiver.getId(), messageBody);
        dispatchMessages(sender, receiver, chatMessage);
    }

    private void dispatchMessages(User sender, User receiver, ChatMessage chatMessage) {
        Map<String, List<Message>> receiverInbox = receiver.getInbox();
        Map<String, List<Message>> sendersSentMessages = sender.getSentMessages();
        if (receiverInbox.containsKey(sender.getId())){
            receiverInbox.get(sender.getId()).add(chatMessage);
        }
        else{
            receiverInbox.put(sender.getId(), Arrays.asList(chatMessage));
        }

        if (sendersSentMessages.containsKey(receiver.getId())){
            sendersSentMessages.get(receiver.getId()).add(chatMessage);
        }
        else {
            sendersSentMessages.put(receiver.getId(), Arrays.asList(chatMessage));
        }
    }
}
