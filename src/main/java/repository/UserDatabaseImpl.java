package repository;
import exceptions.UserException;
import lombok.Getter;
import models.User;
import services.UserService;

import java.util.*;

public class UserDatabaseImpl<K extends Storable> implements Database<K>{
    private final List<K> dataStore;
    @Getter
    private final Set<String> emails;

    private UserDatabaseImpl(){
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

    @Override
    public Optional<K> findByName(String suppliedInput) {
        for(K k : dataStore){
            if(k.getName().contains(suppliedInput.toUpperCase())){
                return Optional.of(k);
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

    private static class UserDatabaseImplSingletonHelper{
        final static UserDatabaseImpl<User> instance = new UserDatabaseImpl<>();
    }

    public static UserDatabaseImpl<?> getInstance(){
        return UserDatabaseImplSingletonHelper.instance;
    }
}
