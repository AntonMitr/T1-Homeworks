package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.clientprocessing.service.impl.CardRequestService;
import by.t1.kotor.common.aop.annotation.HttpIncomeRequestLog;
import by.t1.kotor.common.aop.annotation.HttpOutcomeRequestLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardRequestService cardRequestService;

    @PostMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardRequest request) {
        cardRequestService.sendCardCreateMessage(request);
        return ResponseEntity.accepted().build();
    }
}
