package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.common.model.dto.CardMessage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CardMapper {

    CardMessage toMessage(CardRequest request);
}
