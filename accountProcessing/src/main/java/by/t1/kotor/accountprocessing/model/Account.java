package by.t1.kotor.accountprocessing.model;

import by.t1.kotor.accountprocessing.model.enums.AccountStatusEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "balance", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "is_recalc")
    private Boolean isRecalc;

    @Column(name = "card_exist")
    private Boolean cardExist;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatusEnum status;

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Card> cards = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
}
