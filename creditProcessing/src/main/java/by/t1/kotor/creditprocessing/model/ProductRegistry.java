package by.t1.kotor.creditprocessing.model;

import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_registry")
public class ProductRegistry extends BaseEntity {

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column (name = "month_count")
    private Integer monthCount;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Builder.Default
    @OneToMany(mappedBy = "productRegistry", fetch = FetchType.LAZY)
    private List<PaymentRegistry> paymentRegistryList = new ArrayList<>();
}
