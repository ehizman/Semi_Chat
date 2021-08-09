package repository;

import java.util.List;
import java.util.Optional;

public interface Database<K> {
        void save(K k);


        boolean contains(K k);

        void delete(K k);

        List<K> findAll();

        int size();

        Optional<K> findById(String storableId);

        Optional<K> findByName(String receiverBank);

        void deleteAll();//implemented for the sole purpose of clearing the database after each test
}
