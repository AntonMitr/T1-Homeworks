package by.t1.kotor.creditprocessing.repository;

import by.t1.kotor.creditprocessing.model.ProductRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
}
