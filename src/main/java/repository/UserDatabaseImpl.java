package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDatabaseImpl<K extends Storable> implements Database<K>{
    List<K> dataStore = new ArrayList<>();

    @Override
    public void save(K k) {
        dataStore.add(k);
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
    public Optional<K> findByName(String receiverBank) {
        return Optional.empty();
    }
}
