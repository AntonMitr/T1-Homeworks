package by.t1.kotor.clientprocessing.repository;

import by.t1.kotor.clientprocessing.model.BlacklistRegistry;
import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    boolean existsBlacklistRegistriesByDocumentTypeAndDocumentId(DocumentTypeEnum documentType, String documentId);
}
