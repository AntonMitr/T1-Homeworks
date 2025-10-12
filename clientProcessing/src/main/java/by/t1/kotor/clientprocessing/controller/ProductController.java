package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;
import by.t1.kotor.clientprocessing.service.ProductService;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpIncomeRequestLog;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpOutcomeRequestLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request
    ) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    @PreAuthorize("hasRole({'MASTER', 'GRAND_EMPLOYEE'})")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id,
                                                         @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    @PreAuthorize("hasRole({'MASTER', 'GRAND_EMPLOYEE'})")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
