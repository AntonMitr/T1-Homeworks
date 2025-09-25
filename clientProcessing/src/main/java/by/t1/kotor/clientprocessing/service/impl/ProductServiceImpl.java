package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.mapper.ProductMapper;
import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.dto.product.ProductRequest;
import by.t1.kotor.clientprocessing.model.dto.product.ProductResponse;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.clientprocessing.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return productMapper.toDto(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product = productMapper.partialUpdate(request, product);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }

        productRepository.deleteById(id);
    }

}
