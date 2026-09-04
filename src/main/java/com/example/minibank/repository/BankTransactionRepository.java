package com.example.minibank.repository;

import com.example.minibank.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByStatus(String status);

    List<BankTransaction> findByFromAccountOrToAccount(
            String fromAccount,
            String toAccount);
}