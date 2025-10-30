package by.t1.kotor.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentRegistryMessage {
    private Long accountId;
    private BigDecimal interestRate;
    private Integer monthCount;
    private BigDecimal amount;
    private LocalDate paymentExpirationDate;

}
