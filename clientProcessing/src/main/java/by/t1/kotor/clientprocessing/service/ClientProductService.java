package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import org.springframework.data.domain.Page;

public interface ClientProductService {
    ClientProductResponse create(ClientProductRequest request);

    Page<ClientProductResponse> getAll(int page, int size);

    ClientProductResponse getById(Long id);

    ClientProductResponse update(Long id, ClientProductUpdate clientProductUpdate);

    void delete(Long id);
}
