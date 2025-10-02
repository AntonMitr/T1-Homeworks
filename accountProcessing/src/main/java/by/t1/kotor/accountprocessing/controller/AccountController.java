package by.t1.kotor.accountprocessing.controller;

import by.t1.kotor.accountprocessing.model.Account;
import by.t1.kotor.accountprocessing.service.AccountService;
import by.t1.kotor.common.aop.annotation.HttpIncomeRequestLog;
import by.t1.kotor.common.aop.annotation.HttpOutcomeRequestLog;
import by.t1.kotor.common.model.dto.ClientProductMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @HttpOutcomeRequestLog
    @HttpIncomeRequestLog
    public ResponseEntity<Long> createAccount(@RequestBody ClientProductMessage request) {
        Account account = accountService.createAccount(request);

        return ResponseEntity.ok(account.getId());
    }
}
