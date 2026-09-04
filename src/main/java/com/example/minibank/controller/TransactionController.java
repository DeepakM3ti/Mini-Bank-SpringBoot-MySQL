package com.example.minibank.controller;

import com.example.minibank.entity.BankTransaction;
import com.example.minibank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final BankTransactionRepository bankTransactionRepository;

    @GetMapping
    public Page<BankTransaction> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return bankTransactionRepository.findAll(pageable);
    }

    @GetMapping("/search")
    public List<BankTransaction> searchTransactions(
            @RequestParam String account) {

        return bankTransactionRepository
                .findByFromAccountOrToAccount(account, account);
    }
}