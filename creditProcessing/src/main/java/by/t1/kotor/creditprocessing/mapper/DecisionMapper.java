package by.t1.kotor.creditprocessing.mapper;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.common.model.dto.CreditDecisionMessage;
import by.t1.kotor.creditprocessing.model.Reason;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface DecisionMapper {

    CreditDecisionMessage toMessage(ClientProductMessage message, boolean approved, Reason reason);
}
