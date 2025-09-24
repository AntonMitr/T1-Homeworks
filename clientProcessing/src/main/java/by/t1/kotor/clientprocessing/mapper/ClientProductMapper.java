package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductMessage;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientProductMapper {

    @Mapping(target = "clientId", expression = "java(clientProduct.getClient().getId())")
    @Mapping(target = "productId", expression = "java(clientProduct.getProduct().getId())")
    @Mapping(target = "status", expression = "java(clientProduct.getStatus())")
    @Mapping(target = "openDate", expression = "java(clientProduct.getOpenDate())")
    @Mapping(target = "closeDate", expression = "java(clientProduct.getCloseDate())")
    ClientProductResponse toDto(ClientProduct clientProduct);

    @Mapping(target = "clientId", expression = "java(clientProduct.getClient().getId())")
    @Mapping(target = "productId", expression = "java(clientProduct.getProduct().getId())")
    @Mapping(target = "status", expression = "java(clientProduct.getStatus() != null ? clientProduct.getStatus().name() : null)")
    ClientProductMessage toMessage(ClientProduct clientProduct);

    ClientProduct toEntity(ClientProductRequest clientProductRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientProduct partialUpdate(ClientProductUpdate request, @MappingTarget ClientProduct clientProduct);


}
