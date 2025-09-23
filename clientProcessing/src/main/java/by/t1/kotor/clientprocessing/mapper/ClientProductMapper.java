package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.dto.ClientResponse;
import by.t1.kotor.clientprocessing.model.dto.ProductRequest;
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

    ClientProductResponse toDto(ClientProduct clientProduct);

    @Mapping(target = "clientId", expression = "java(clientProduct.getClient().getId())")
    @Mapping(target = "productId", expression = "java(clientProduct.getProduct().getId())")
    ClientProductMessage toMessage(ClientProduct clientProduct);

    ClientProduct toEntity(ClientProductRequest clientProductRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientProduct partialUpdate(ClientProductUpdate request, @MappingTarget ClientProduct clientProduct);


}
