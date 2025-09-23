package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.ProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.repository.ClientProductRepository;
import by.t1.kotor.clientprocessing.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client-products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService clientProductService;

    @PostMapping
    public ResponseEntity<ClientProductResponse> addClientProduct(
            @RequestBody ClientProductRequest request
    ) {
        ClientProductResponse clientProductResponse = clientProductService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientProductResponse);
    }
}
