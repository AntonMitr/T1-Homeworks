package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.client.ClientRegistrationRequest;
import by.t1.kotor.clientprocessing.model.dto.client.ClientResponse;
import by.t1.kotor.clientprocessing.service.ClientService;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpIncomeRequestLog;
import by.t1.kotor.crosscuttingstarter.aop.annotation.HttpOutcomeRequestLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ClientResponse> register(
            @Valid @RequestBody ClientRegistrationRequest request
    ) {
        ClientResponse response = clientService.registerClient(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<ClientResponse> getById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(clientService.findById(id));
    }
}
