package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.dto.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientMapper {

    Client toEntity(ClientRegistrationRequest clientDto);

    ClientResponse toDto(Client client);

}
