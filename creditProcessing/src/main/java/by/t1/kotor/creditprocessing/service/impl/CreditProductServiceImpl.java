package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import by.t1.kotor.creditprocessing.service.CreditProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditProductServiceImpl implements CreditProductService {

    private final ProductRegistryRepository productRegistryRepository;


}
