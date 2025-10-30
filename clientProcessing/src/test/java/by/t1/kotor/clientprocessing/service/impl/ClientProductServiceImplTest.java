package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.exception.ClientNotFoundException;
import by.t1.kotor.clientprocessing.exception.ClientProductNotFoundException;
import by.t1.kotor.clientprocessing.exception.ProductNotFoundException;
import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.ClientProductMapper;
import by.t1.kotor.clientprocessing.model.Client;
import by.t1.kotor.clientprocessing.model.ClientProduct;
import by.t1.kotor.clientprocessing.model.Product;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductRequest;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductResponse;
import by.t1.kotor.clientprocessing.model.dto.clientProduct.ClientProductUpdate;
import by.t1.kotor.clientprocessing.model.enums.KeyEnum;
import by.t1.kotor.clientprocessing.repository.ClientProductRepository;
import by.t1.kotor.clientprocessing.repository.ClientRepository;
import by.t1.kotor.clientprocessing.repository.ProductRepository;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientProductServiceImplTest {
    @Mock
    private ClientProductRepository clientProductRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private KafkaProducer<ClientProductMessage> productKafkaProducer;
    @Mock
    private ClientProductMapper clientProductMapper;

    @InjectMocks
    private ClientProductServiceImpl service;

    private Client client;
    private Product product;
    private ClientProductRequest request;
    private ClientProduct entity;
    private ClientProduct saved;
    private ClientProductMessage message;
    private ClientProductResponse dto;

    @BeforeEach
    void setUp() {
        client = new Client();
        product = new Product();
        product.setKey(KeyEnum.DC);

        request = ClientProductRequest.builder()
                .clientId(1L)
                .productId(1L)
                .build();

        entity = new ClientProduct();
        saved = new ClientProduct();
        message = ClientProductMessage.builder().build();
        dto = ClientProductResponse.builder().build();
    }

    @Test
    void create_shouldThrowClientNotFoundException_whenClientMissing() {
        when(clientRepository.findById(request.clientId())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> service.create(request));

        verify(clientRepository).findById(request.clientId());
        verifyNoInteractions(productRepository, clientProductRepository, productKafkaProducer, clientProductMapper);
    }

    @Test
    void create_shouldThrowProductNotFoundException_whenProductMissing() {
        when(clientRepository.findById(request.clientId())).thenReturn(Optional.of(client));
        when(productRepository.findById(request.productId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.create(request));

        verify(clientRepository).findById(request.clientId());
        verify(productRepository).findById(request.productId());
        verifyNoInteractions(clientProductRepository, productKafkaProducer, clientProductMapper);
    }

    @Test
    void create_shouldSaveClientProduct_whenValid() {
        when(clientRepository.findById(request.clientId())).thenReturn(Optional.of(client));
        when(productRepository.findById(request.productId())).thenReturn(Optional.of(product));
        when(clientProductMapper.toEntity(request)).thenReturn(entity);
        when(clientProductRepository.save(entity)).thenReturn(saved);
        when(clientProductMapper.toMessage(request)).thenReturn(message);
        when(clientProductMapper.toDto(saved)).thenReturn(dto);

        ClientProductResponse result = service.create(request);

        assertEquals(dto, result);
        verify(clientProductRepository).save(entity);
    }

    @Test
    void getEntity_shouldThrowClientProductNotFoundException_whenMissing() {
        Long id = 1L;
        when(clientProductRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientProductNotFoundException.class, () -> service.getById(id));
    }
}