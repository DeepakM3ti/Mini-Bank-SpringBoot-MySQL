package com.example.minibank.repository;

import com.example.minibank.entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository
        extends JpaRepository<BankAccount, Long> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmail(String email);

    Optional<BankAccount> findByAccountNumber(
            String accountNumber);

    Page<BankAccount> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);
}