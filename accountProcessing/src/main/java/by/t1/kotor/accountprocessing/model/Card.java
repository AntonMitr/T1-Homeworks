package by.t1.kotor.accountprocessing.model;

import by.t1.kotor.accountprocessing.model.enums.CardStatusEnum;
import by.t1.kotor.accountprocessing.model.enums.PaymentSystemEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "card_id", unique = true)
    private String cardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system")
    private PaymentSystemEnum PaymentSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardStatusEnum status;

    @Builder.Default
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
}
