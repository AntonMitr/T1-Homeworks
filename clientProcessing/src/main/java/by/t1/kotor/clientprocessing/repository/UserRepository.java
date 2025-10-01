package by.t1.kotor.clientprocessing.repository;

import by.t1.kotor.clientprocessing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
