package com.example.minibank.service;

import com.example.minibank.entity.BankTransaction;
import com.example.minibank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final BankTransactionRepository bankTransactionRepository;

    @Override
    @Transactional
    public void updatePendingTransactions() {

        List<BankTransaction> transactions =
                bankTransactionRepository.findByStatus("PENDING");

        for (BankTransaction transaction : transactions) {

            log.info(
                    "Updating pending transaction ID: {}",
                    transaction.getId()
            );

            transaction.setStatus("COMPLETED");

            bankTransactionRepository.save(transaction);

            log.info(
                    "Transaction ID {} updated to COMPLETED",
                    transaction.getId()
            );
        }
    }
}