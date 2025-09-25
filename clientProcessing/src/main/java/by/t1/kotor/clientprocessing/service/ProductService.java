package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    Page<ProductResponse> getAll(int page, int size);
    ProductResponse getById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
