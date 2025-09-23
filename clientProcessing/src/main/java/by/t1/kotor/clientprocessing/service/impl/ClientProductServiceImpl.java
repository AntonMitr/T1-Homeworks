package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.ClientProductMapper;
import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductMessage;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import by.t1.kotor.clientprocessing.repository.ClientProductRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.clientprocessing.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ClientProduct clientProduct = ClientProduct.builder()
                .client(client)
                .product(product)
                .openDate(LocalDate.now())
                .status(StatusEnum.ACTIVE)
                .build();

        ClientProduct saved = clientProductRepository.save(clientProduct);

        //отправляем сообщение kafka
        String topic = getTopicByProductKey(product.getKey().name());
        productKafkaProducer.sendTo(topic, clientProductMapper.toMessage(saved));

        return clientProductMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientProductResponse> getAll() {
        return clientProductRepository.findAll()
                .stream()
                .map(clientProductMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientProductResponse getById(Long id) {
        ClientProduct clientProduct = getEntity(id);

        return clientProductMapper.toDto(clientProduct);
    }

    @Override
    public ClientProductResponse update(Long id, ClientProductUpdate clientProductUpdate) {
        ClientProduct clientProduct = getEntity(id);

        clientProductMapper.partialUpdate(clientProductUpdate, clientProduct);

        ClientProduct updated = clientProductRepository.save(clientProduct);

        //кафка сообщение
        String topic = getTopicByProductKey(updated.getProduct().getKey().name());
        productKafkaProducer.sendTo(topic, clientProductMapper.toMessage(updated));

        return clientProductMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        ClientProduct clientProduct = getEntity(id);

        clientProductRepository.delete(clientProduct);

        // сообщение о удалении
        String topic = getTopicByProductKey(clientProduct.getProduct().getKey().name());
        productKafkaProducer.sendTo(topic, clientProductMapper.toMessage(clientProduct));
    }

    private ClientProduct getEntity(Long id) {
        return clientProductRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClientProduct not found"));
    }

    private String getTopicByProductKey(String key) {
        return switch (key) {
            case "DC", "CC", "NS", "PENS" -> clientProductsTopic;
            case "IPO", "PC", "AC" -> clientCreditProductsTopic;
            default -> throw new IllegalArgumentException("Unknown product key: " + key);
        };
    }
}
