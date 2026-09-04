package com.example.minibank.mapper;

import org.springframework.stereotype.Component;

import com.example.minibank.dto.BankAccountDTO;
import com.example.minibank.entity.BankAccount;

@Component
public class BankAccountMapper {

    public BankAccount toEntity(BankAccountDTO dto) {

        BankAccount account = new BankAccount();

        account.setId(dto.getId());
        account.setAccountNumber(dto.getAccountNumber());
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setBalance(dto.getBalance());
        account.setAccountType(dto.getAccountType());

        return account;
    }

    public BankAccountDTO toDTO(BankAccount account) {

        BankAccountDTO dto = new BankAccountDTO();

        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setName(account.getName());
        dto.setEmail(account.getEmail());
        dto.setPhone(account.getPhone());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType());

        return dto;
    }
}