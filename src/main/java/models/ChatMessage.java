package models;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Message{
    @Getter
    private final String chatMessage;
    @Getter
    private final String senderName;
    @Getter
    private final String senderId;
    @Getter
    private final String receiverId;
    @Getter
    private final LocalDateTime sendTime;


    public ChatMessage(String senderName, String senderId, String receiverId, String messageBody){
        this.senderName = senderName;
        this.chatMessage = messageBody;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sendTime = LocalDateTime.now();
    }

    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd:HH/mm");
        return (String.format("""
                Sender -> %s
                --------------------
                Message -> %s
                Time Delivered -> %s""", this.getSenderName(), this.getChatMessage(),formatter.format(getSendTime())));
    }
}
