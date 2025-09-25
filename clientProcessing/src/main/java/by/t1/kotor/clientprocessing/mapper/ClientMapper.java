package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientMapper {

    @Mapping(target = "clientId", expression = "java(generateClientId())")
    Client toEntity(ClientRegistrationRequest clientDto);

    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.email", target = "email")
    ClientResponse toDto(Client client);

    default String generateClientId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
