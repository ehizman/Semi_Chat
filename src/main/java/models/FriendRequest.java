package models;

import lombok.Getter;

import java.time.LocalDateTime;

public final class FriendRequest implements Message<FriendRequest>{
        private final String senderName;
        @Getter
        private final String senderId;
        @Getter
        private final String receiverId;
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
            int year = timeSent.getYear();
            int month = timeSent.getMonthValue();
            int day = timeSent.getDayOfMonth();
            int hour = timeSent.getHour();
            int minute = timeSent.getMinute();
            return String.format("You have received a friend request from %s at %s", senderName, String.format("%d-%d-%d:%02d:%02d",year, month, day, hour, minute));
        }
    }