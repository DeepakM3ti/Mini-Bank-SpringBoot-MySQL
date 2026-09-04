package com.example.minibank.scheduler;

import com.example.minibank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionScheduler {

    private final TransactionService transactionService;

    @Scheduled(fixedRate = 60000)
    public void updatePendingTransactions() {

        log.info("Checking pending transactions...");

        transactionService.updatePendingTransactions();

        log.info("Pending transaction check completed");
    }
}