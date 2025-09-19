package by.t1.kotor.accountprocessing.repository;

import by.t1.kotor.accountprocessing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
