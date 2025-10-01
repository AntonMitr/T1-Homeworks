package by.t1.kotor.creditprocessing.mapper;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.model.ProductRegistry;
import by.t1.kotor.creditprocessing.model.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProductMapper {

    @Mapping(target = "openDate", expression = "java(java.time.LocalDate.now())")
    ProductRegistry toEntity(ClientProductMessage message);

    ProductResponse toDTO(ProductRegistry productRegistry);
}
