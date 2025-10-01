package by.t1.kotor.creditprocessing.mapper;

import by.t1.kotor.common.model.dto.PaymentRegistryMessage;
import by.t1.kotor.creditprocessing.model.PaymentRegistry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PaymentMapper {
    @Mapping(target = "accountId", source = "productRegistry.accountId")
    @Mapping(target = "interestRate", source = "productRegistry.interestRate")
    @Mapping(target = "monthCount", source = "productRegistry.monthCount")
    PaymentRegistryMessage toMessage(PaymentRegistry paymentRegistry);
}
