package by.t1.kotor.clientprocessing.model;

import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blacklist_registry")
public class BlacklistRegistry extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentTypeEnum documentType;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "blacklisted_at")
    private LocalDateTime blacklistedAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklist_expiration_date")
    private LocalDateTime expirationDate;
}
