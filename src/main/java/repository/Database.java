package repository;
import java.util.List;
import java.util.Optional;

public interface Database<K> {
        void checkEmail(String email);

        void addNewEmail(String email);

        void save(K k);

        boolean contains(K k);

        void delete(K k);

        List<K> findAll();

        int size();

        Optional<K> findById(String storableId);


        void deleteAll();//implemented for the sole purpose of clearing the database after each test

        Optional<List<K>> findAllByName(String namePattern);

        Optional<K> findByEmail(String email);

        Optional<List<K>> findAllMembersThatBelongToGroupWithThisId(String id);
}
