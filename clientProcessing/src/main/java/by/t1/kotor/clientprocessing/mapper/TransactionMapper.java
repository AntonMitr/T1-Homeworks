package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.dto.transaction.TransactionRequest;
import by.t1.kotor.common.model.dto.TransactionMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TransactionMapper {

    @Mapping(target = "timeStamp", expression = "java(java.time.LocalDateTime.now())")
    TransactionMessage toMessage(TransactionRequest transactionRequest);
}
