package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.ProductNotFoundException;
import by.t1.kotor.clientprocessing.mapper.ProductMapper;
import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;
import by.t1.kotor.clientprocessing.model.enums.KeyEnum;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.clientprocessing.service.ProductService;
import by.t1.kotor.crosscuttingstarter.aop.annotation.Cached;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    @LogDatasourceError
    public ProductResponse create(ProductRequest request) {
        log.info("Creating product: {}", request);
        Product product = productMapper.toEntity(request);
        productRepository.save(product);
        log.info("Product created successfully: {}", product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    @LogDatasourceError
    public Page<ProductResponse> getAll(int page, int size) {
        log.info("Fetching all products, page={}, size={}", page, size);
        return productRepository.findAll(PageRequest.of(page, size))
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @LogDatasourceError
    @Cached
    public ProductResponse getById(Long id) {
        log.info("Fetching product by id={}", id);
        Product product = getProductById(id);
        return productMapper.toDto(product);
    }

    @Override
    @LogDatasourceError
    public ProductResponse update(Long id, ProductRequest request) {
        log.info("Updating product id={}, request={}", id, request);
        Product product = getProductById(id);

        product = productMapper.partialUpdate(request, product);
        productRepository.save(product);
        log.info("Product updated successfully: {}", product);

        return productMapper.toDto(product);
    }

    @Override
    @LogDatasourceError
    public void delete(Long id) {
        log.info("Deleting product id={}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Product not found: id={}", id);
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully id={}", id);
    }

    @Override
    public int countDeposits() {
        return productRepository.countByKey(KeyEnum.CC);
    }

    @Override
    public int countCreditCards() {
        return productRepository.countByKey(KeyEnum.DC);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found: id={}", productId);
                    return new ProductNotFoundException(productId);
                });
    }
}
