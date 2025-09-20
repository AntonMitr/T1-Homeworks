package by.t1.kotor.accountprocessing.model;

import by.t1.kotor.accountprocessing.model.enums.TransactionStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.TransactionTypeEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionTypeEnum type;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatusEnum status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
