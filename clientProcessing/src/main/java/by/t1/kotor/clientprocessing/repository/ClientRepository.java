package by.t1.kotor.clientprocessing.repository;

import by.t1.kotor.clientprocessing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
