package com.example.minibank.controller;

import com.example.minibank.dto.BankAccountDTO;
import com.example.minibank.dto.TransferRequest;
import com.example.minibank.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public BankAccountDTO createAccount(
            @Valid @RequestBody BankAccountDTO dto) {

        return bankAccountService.createAccount(dto);
    }

    @GetMapping("/{id}")
    public BankAccountDTO getAccountById(
            @PathVariable Long id) {

        return bankAccountService.getAccountById(id);
    }

    @GetMapping
    public Page<BankAccountDTO> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bankAccountService.getAllAccounts(
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/search")
    public Page<BankAccountDTO> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bankAccountService.searchByName(
                name,
                page,
                size,
                sortBy,
                direction);
    }

    @PutMapping("/{id}")
    public BankAccountDTO updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody BankAccountDTO dto) {

        return bankAccountService.updateAccount(id, dto);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteAccount(
            @PathVariable Long id) {

        bankAccountService.deleteAccount(id);

        return Map.of(
                "message",
                "Account deleted successfully");
    }

    @PutMapping("/{id}/deposit")
    public BankAccountDTO deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        return bankAccountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public BankAccountDTO withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        return bankAccountService.withdraw(id, amount);
    }

    @PostMapping("/transfer")
    public Map<String, String> transferMoney(
            @Valid @RequestBody TransferRequest request) {

        bankAccountService.transferMoney(request);

        return Map.of(
                "message",
                "Money transferred successfully");
    }
}