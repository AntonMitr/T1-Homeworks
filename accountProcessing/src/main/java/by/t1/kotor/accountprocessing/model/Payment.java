package by.t1.kotor.accountprocessing.model;

import by.t1.kotor.accountprocessing.model.enums.PaymentTypeEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "is_credit")
    private Boolean isCredit;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PaymentTypeEnum type;
}
