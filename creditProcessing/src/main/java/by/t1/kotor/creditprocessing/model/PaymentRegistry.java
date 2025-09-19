package by.t1.kotor.creditprocessing.model;

import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_registry")
public class PaymentRegistry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_registry_id", nullable = false)
    private ProductRegistry productRegistry;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate_amount", precision = 5, scale = 4)
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount", precision = 18, scale = 2)
    private BigDecimal debtAmount;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "payment_expiration_date")
    private LocalDate paymentExpirationDate;
}
