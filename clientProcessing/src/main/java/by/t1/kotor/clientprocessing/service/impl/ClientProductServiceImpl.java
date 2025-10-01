package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.ClientNotFoundException;
import by.t1.kotor.clientprocessing.exception.ClientProductNotFoundException;
import by.t1.kotor.clientprocessing.exception.ProductNotFoundException;
import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.ClientProductMapper;
import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import by.t1.kotor.clientprocessing.repository.ClientProductRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.clientprocessing.service.ClientProductService;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ClientProductRepository clientProductRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final KafkaProducer<ClientProductMessage> productKafkaProducer;
    private final ClientProductMapper clientProductMapper;

    @Value("${t1.kafka.topic.client_products}")
    private String clientProductsTopic;
    @Value("${t1.kafka.topic.client_credit_products}")
    private String clientCreditProductsTopic;

    @Override
    public ClientProductResponse create(ClientProductRequest request) {
        log.info("Creating ClientProduct: {}", request);

        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> {
                    log.warn("Client not found: id={}", request.clientId());
                    return new ClientNotFoundException(request.clientId());
                });

        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> {
                    log.warn("Product not found: id={}", request.productId());
                    return new ProductNotFoundException(request.productId());
                });

        ClientProduct clientProduct = clientProductMapper.toEntity(request);
        clientProduct.setClient(client);
        clientProduct.setProduct(product);

        ClientProduct saved = clientProductRepository.save(clientProduct);
        log.debug("Saved ClientProduct entity: {}", saved);

        String topic = getTopicByProductKey(product.getKey().name());
        productKafkaProducer.sendTo(topic, clientProductMapper.toMessage(request));
        log.info("Sent ClientProductMessage to topic {}: {}", topic, request);

        return clientProductMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientProductResponse> getAll(int page, int size) {
        log.info("Fetching all ClientProducts, page={}, size={}", page, size);
        return clientProductRepository.findAll(PageRequest.of(page, size))
                .map(clientProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientProductResponse getById(Long id) {
        log.info("Fetching ClientProduct by id={}", id);
        ClientProduct clientProduct = getEntity(id);
        return clientProductMapper.toDto(clientProduct);
    }

    @Override
    public ClientProductResponse update(Long id, ClientProductUpdate clientProductUpdate) {
        log.info("Updating ClientProduct id={}, update={}", id, clientProductUpdate);
        ClientProduct clientProduct = getEntity(id);

        clientProductMapper.partialUpdate(clientProductUpdate, clientProduct);
        ClientProduct updated = clientProductRepository.save(clientProduct);
        log.debug("Updated ClientProduct entity: {}", updated);

        return clientProductMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting ClientProduct id={}", id);
        ClientProduct clientProduct = getEntity(id);
        clientProductRepository.delete(clientProduct);
        log.info("Deleted ClientProduct id={}", id);
    }

    private ClientProduct getEntity(Long id) {
        return clientProductRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ClientProduct not found: id={}", id);
                    return new ClientProductNotFoundException(id);
                });
    }

    private String getTopicByProductKey(String key) {
        return switch (key) {
            case "DC", "CC", "NS", "PENS" -> clientProductsTopic;
            case "IPO", "PC", "AC" -> clientCreditProductsTopic;
            default -> {
                log.error("Unknown product key: {}", key);
                throw new IllegalArgumentException("Unknown product key: " + key);
            }
        };
    }
}
