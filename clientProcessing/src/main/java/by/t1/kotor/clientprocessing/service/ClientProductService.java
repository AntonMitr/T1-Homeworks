package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;

import java.util.List;

public interface ClientProductService {
    ClientProductResponse create(ClientProductRequest request);
    List<ClientProductResponse> getAll();
    ClientProductResponse getById(Long id);
    ClientProductResponse update(Long id, ClientProductUpdate clientProductUpdate);
    void delete(Long id);
}
