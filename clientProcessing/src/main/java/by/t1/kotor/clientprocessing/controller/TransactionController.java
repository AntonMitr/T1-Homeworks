package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.transaction.TransactionRequest;
import by.t1.kotor.clientprocessing.service.impl.TransactionRequestService;
import by.t1.kotor.common.model.dto.TransactionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRequestService transactionRequestService;

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionRequestService.sendCardCreateMessage(transactionRequest);
        return ResponseEntity.accepted().build();
    }
}
