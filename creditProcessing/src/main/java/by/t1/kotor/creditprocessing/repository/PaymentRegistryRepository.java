package by.t1.kotor.creditprocessing.repository;

import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {
}
