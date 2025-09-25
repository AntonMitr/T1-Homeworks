package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductMessage;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ClientProductMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productId", source = "product.id")
    ClientProductResponse toDto(ClientProduct clientProduct);


    @Mapping(target = "openDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "status", expression = "java(by.t1.kotor.clientprocessing.model.enums.StatusEnum.ACTIVE)")
    ClientProduct toEntity(ClientProductRequest request);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productId", source = "product.id")
    ClientProductMessage toMessage(ClientProduct clientProduct);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientProduct partialUpdate(ClientProductUpdate request, @MappingTarget ClientProduct clientProduct);
}
