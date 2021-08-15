package services;

import models.Message;
import models.User;

public interface MessageDispatcher {
    void send(User receiver, Message<?> message);
}
