package models;

public interface Observable {

    void subscribe(String... userId);
    void removeObserver(String userId);
    void broadcast(Message message);
}
