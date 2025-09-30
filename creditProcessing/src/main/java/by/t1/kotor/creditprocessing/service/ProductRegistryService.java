package by.t1.kotor.creditprocessing.service;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.model.dto.ProductResponse;

public interface ProductRegistryService {
    ProductResponse create(ClientProductMessage message);
}
