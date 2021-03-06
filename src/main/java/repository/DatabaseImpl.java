package repository;
import exceptions.UserException;
import lombok.Getter;
import models.User;

import java.util.*;

public class DatabaseImpl<K extends Storable> implements Database<K>{
    private final List<K> dataStore;
    @Getter
    private final Set<String> emails;

    private DatabaseImpl(){
        this.dataStore = new ArrayList<>();
        this.emails = new HashSet<>();
    }

    @Override
    public void save(K k) {
        dataStore.add(k);
    }
    @Override
    public void checkEmail(String email) {
        if (emails.contains(email)){
            throw new UserException("Email already exists");
        }
    }

    @Override
    public void addNewEmail(String email) {
        emails.add(email);
    }



    @Override
    public boolean contains(K k) {
      return dataStore.contains(k);
    }

    @Override
    public void delete(K k) {
        dataStore.remove(k);
    }

    @Override
    public List<K> findAll() {
        return dataStore;
    }

    @Override
    public int size() {
        return dataStore.size();
    }

    @Override
    public Optional<K> findById(String storableId) {
        for (K user : dataStore){
                if (user.getId().equals(storableId)){
                    return Optional.of(user);
                }
            }
        return Optional.empty();
    }

    //implemented for the sole purpose of clearing the database after each test
    @Override
    public void deleteAll() {
        dataStore.clear();
    }

    @Override
    public Optional<List<K>> findAllByName(String namePattern) {
        List<K> listOfUserNamesThatContainNamePattern = new ArrayList<>();
        for (K k : dataStore){
            if (k.getName().contains(namePattern.toUpperCase())){
                listOfUserNamesThatContainNamePattern.add(k);
            }
        }
        if (listOfUserNamesThatContainNamePattern.size() != 0){
            return Optional.of(listOfUserNamesThatContainNamePattern);
        }
        else{
            return Optional.empty();
        }
    }

    @Override
    public Optional<K> findByEmail(String email) {
        for(K user : dataStore){
            if (user.getEmail().equals(email.toLowerCase())){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<K>> findAllMembersThatBelongToGroupWithThisId(String id) {
        List<User> listOfUsersThatBelongToChatRoom = new ArrayList<>();
        for (K k: dataStore) {
            User user = (User)k;
            if (user.getChatRooms().contains(id)){
                listOfUsersThatBelongToChatRoom.add(user);
            }
        }
        return Optional.of((List<K>) listOfUsersThatBelongToChatRoom);
    }

    private static class UserDatabaseImplSingletonHelper{
        final static DatabaseImpl<User> instance = new DatabaseImpl<>();
    }

    public static DatabaseImpl<?> getInstance(){
        return UserDatabaseImplSingletonHelper.instance;
    }
}
