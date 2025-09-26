package by.t1.kotor.creditprocessing.repository;

import by.t1.kotor.creditprocessing.model.ProductRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
    @Query("SELECT pr FROM ProductRegistry pr LEFT JOIN FETCH pr.paymentRegistryList WHERE pr.clientId = :clientId")
    List<ProductRegistry> findByClientId(@Param("clientId") Long clientId);
}
