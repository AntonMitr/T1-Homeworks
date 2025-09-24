package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
