package models;

public interface Observable {

    public void register(String userId);
    public void removeObserver(String userId);
    public void broadcast(Message message);
}
