package by.t1.kotor.clientprocessing.controller;

import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.clientprocessing.service.CardService;
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

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Void> createCard(@RequestBody CardRequest request) {
        cardService.sendCardCreateMessage(request);
        return ResponseEntity.accepted().build();
    }
}
