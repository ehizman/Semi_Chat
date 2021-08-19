package models;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FriendRequest implements Message{
        private final String senderName;
        @Getter
        private final String senderId;
        @Getter
        private final String receiverId;
        @Getter
        private final LocalDateTime timeSent;
        @Getter
        private final RequestStatus requestStatus = RequestStatus.PENDING;
        
        public FriendRequest(String senderName, String senderId, String receiverId){
            this.senderName = senderName;
            this.timeSent = LocalDateTime.now();
            this.senderId = senderId;
            this.receiverId = receiverId;
        }

        public String toString(){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd:HH/mm");
            return String.format("You have received a friend request from %s at %s",
                    senderName, formatter.format(timeSent));
        }
    }