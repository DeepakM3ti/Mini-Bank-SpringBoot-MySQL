package com.example.minibank.service;

import com.example.minibank.dto.BankAccountDTO;
import com.example.minibank.dto.TransferRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface BankAccountService {

    BankAccountDTO createAccount(BankAccountDTO dto);

    BankAccountDTO getAccountById(Long id);

    Page<BankAccountDTO> getAllAccounts(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<BankAccountDTO> searchByName(
            String name,
            int page,
            int size,
            String sortBy,
            String direction);

    BankAccountDTO updateAccount(
            Long id,
            BankAccountDTO dto);

    void deleteAccount(Long id);

    BankAccountDTO deposit(
            Long id,
            BigDecimal amount);

    BankAccountDTO withdraw(
            Long id,
            BigDecimal amount);

    void transferMoney(TransferRequest request);
}