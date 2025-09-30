package by.t1.kotor.clientprocessing.model;

import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client extends BaseEntity {

    @Column(name = "client_id")
    private String clientId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentTypeEnum documentType;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "document_prefix")
    private String documentPrefix;

    @Column(name = "document_suffix")
    private String documentSuffix;

    @Builder.Default
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientProduct> clientProducts = new ArrayList<>();
}
