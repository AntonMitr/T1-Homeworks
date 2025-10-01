package by.t1.kotor.accountprocessing.repository;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByAccountId(Long accountId);
    List<Payment> findByAccountIdAndPaymentDateBeforeAndPayedAtIsNull(Long accountId, LocalDate localDate);
    boolean existsByAccountAndPaymentDate(Account account, LocalDate date);
    List<Payment> findByAccountIdAndPayedAtIsNull(Long accountId);
}
