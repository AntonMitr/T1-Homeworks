package by.t1.kotor.accountprocessing.repository;

import by.t1.kotor.accountprocessing.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByClientIdAndProductId(Long clientId, Long productId);
}
