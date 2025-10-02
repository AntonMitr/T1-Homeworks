package by.t1.kotor.accountprocessing.repository;

import by.t1.kotor.accountprocessing.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByAccountId(Long accountId);
}
