package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import by.t1.kotor.clientprocessing.service.ClientProductService;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpIncomeRequestLog;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpOutcomeRequestLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client-products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService clientProductService;

    @PostMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ClientProductResponse> create(
            @Valid @RequestBody ClientProductRequest request
    ) {
        ClientProductResponse response = clientProductService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<Page<ClientProductResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(clientProductService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ClientProductResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientProductService.getById(id));
    }

    @PutMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ClientProductResponse> update(@PathVariable("id") Long id,
                                                        @Valid @RequestBody ClientProductUpdate clientProductUpdate) {
        return ResponseEntity.ok(clientProductService.update(id, clientProductUpdate));
    }

    @DeleteMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        clientProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
