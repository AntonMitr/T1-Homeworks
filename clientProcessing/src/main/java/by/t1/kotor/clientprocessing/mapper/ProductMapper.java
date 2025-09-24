package by.t1.kotor.clientprocessing.mapper;

import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    ProductResponse toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductRequest request, @MappingTarget Product product);
}
