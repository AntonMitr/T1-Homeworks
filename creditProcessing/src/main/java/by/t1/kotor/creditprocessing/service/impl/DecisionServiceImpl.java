package by.t1.kotor.creditprocessing.service.impl;

import by.t1.kotor.creditprocessing.repository.ProductRegistryRepository;
import by.t1.kotor.creditprocessing.service.ClientInfoService;
import by.t1.kotor.creditprocessing.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {
    private final ClientInfoService clientInfoService;
    private final ProductRegistryRepository productRegistryRepository;

}
