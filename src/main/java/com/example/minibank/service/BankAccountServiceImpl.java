package com.example.minibank.service;

import com.example.minibank.dto.BankAccountDTO;
import com.example.minibank.dto.TransferRequest;
import com.example.minibank.entity.BankAccount;
import com.example.minibank.entity.BankTransaction;
import com.example.minibank.exception.DuplicateAccountException;
import com.example.minibank.exception.InsufficientBalanceException;
import com.example.minibank.exception.ResourceNotFoundException;
import com.example.minibank.mapper.BankAccountMapper;
import com.example.minibank.repository.BankAccountRepository;
import com.example.minibank.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final BankAccountMapper bankAccountMapper;

    @Override
    @Transactional
    public BankAccountDTO createAccount(BankAccountDTO dto) {

        log.info("Creating bank account: {}", dto.getAccountNumber());

        if (bankAccountRepository.existsByAccountNumber(dto.getAccountNumber())) {
            log.error("Account number already exists: {}", dto.getAccountNumber());
            throw new DuplicateAccountException("Account number already exists");
        }

        if (bankAccountRepository.existsByEmail(dto.getEmail())) {
            log.error("Email already exists: {}", dto.getEmail());
            throw new DuplicateAccountException("Email already exists");
        }

        BankAccount account = bankAccountMapper.toEntity(dto);

        BankAccount savedAccount =
                bankAccountRepository.save(account);

        log.info("Bank account created successfully with id: {}",
                savedAccount.getId());

        return bankAccountMapper.toDTO(savedAccount);
    }

    @Override
    public BankAccountDTO getAccountById(Long id) {

        log.info("Fetching account with id: {}", id);

        BankAccount account =
                bankAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + id));

        return bankAccountMapper.toDTO(account);
    }

    @Override
    public Page<BankAccountDTO> getAllAccounts(
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info(
                "Fetching accounts - page: {}, size: {}, sortBy: {}, direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return bankAccountRepository
                .findAll(pageable)
                .map(bankAccountMapper::toDTO);
    }

    @Override
    public Page<BankAccountDTO> searchByName(
            String name,
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Searching accounts by name: {}", name);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return bankAccountRepository
                .findByNameContainingIgnoreCase(name, pageable)
                .map(bankAccountMapper::toDTO);
    }

    @Override
    @Transactional
    public BankAccountDTO updateAccount(
            Long id,
            BankAccountDTO dto) {

        log.info("Updating account with id: {}", id);

        BankAccount account =
                bankAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + id));

        if (!account.getAccountNumber().equals(dto.getAccountNumber())
                && bankAccountRepository
                .existsByAccountNumber(dto.getAccountNumber())) {

            throw new DuplicateAccountException(
                    "Account number already exists");
        }

        if (!account.getEmail().equals(dto.getEmail())
                && bankAccountRepository
                .existsByEmail(dto.getEmail())) {

            throw new DuplicateAccountException(
                    "Email already exists");
        }

        account.setAccountNumber(dto.getAccountNumber());
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setAccountType(dto.getAccountType());

        BankAccount updatedAccount =
                bankAccountRepository.save(account);

        log.info("Account updated successfully with id: {}", id);

        return bankAccountMapper.toDTO(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {

        log.info("Deleting account with id: {}", id);

        BankAccount account =
                bankAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + id));

        bankAccountRepository.delete(account);

        log.info("Account deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public BankAccountDTO deposit(
            Long id,
            BigDecimal amount) {

        log.info(
                "Deposit started - account id: {}, amount: {}",
                id,
                amount
        );

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero");
        }

        BankAccount account =
                bankAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + id));

        account.setBalance(
                account.getBalance().add(amount));

        BankAccount updatedAccount =
                bankAccountRepository.save(account);

        log.info(
                "Deposit completed - account id: {}, amount: {}",
                id,
                amount
        );

        return bankAccountMapper.toDTO(updatedAccount);
    }

    @Override
    @Transactional
    public BankAccountDTO withdraw(
            Long id,
            BigDecimal amount) {

        log.info(
                "Withdrawal started - account id: {}, amount: {}",
                id,
                amount
        );

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero");
        }

        BankAccount account =
                bankAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + id));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(amount));

        BankAccount updatedAccount =
                bankAccountRepository.save(account);

        log.info(
                "Withdrawal completed - account id: {}, amount: {}",
                id,
                amount
        );

        return bankAccountMapper.toDTO(updatedAccount);
    }

    @Override
    @Transactional
    public void transferMoney(TransferRequest request) {

        log.info(
                "Transfer started - from: {}, to: {}, amount: {}",
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );

        if (request.getFromAccount()
                .equals(request.getToAccount())) {

            throw new IllegalArgumentException(
                    "Cannot transfer to the same account");
        }

        BankAccount sender =
                bankAccountRepository
                        .findByAccountNumber(
                                request.getFromAccount())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sender account not found"));

        BankAccount receiver =
                bankAccountRepository
                        .findByAccountNumber(
                                request.getToAccount())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receiver account not found"));

        if (sender.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(request.getAmount()));

        receiver.setBalance(
                receiver.getBalance()
                        .add(request.getAmount()));

        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);

        BankTransaction transaction =
                new BankTransaction();

        transaction.setFromAccount(
                request.getFromAccount());

        transaction.setToAccount(
                request.getToAccount());

        transaction.setAmount(
                request.getAmount());

        transaction.setTransactionType(
                "TRANSFER");

        transaction.setTransactionDate(
                LocalDateTime.now());

        transaction.setStatus(
                "PENDING");

        bankTransactionRepository.save(transaction);

        log.info(
                "Transfer completed - from: {}, to: {}, amount: {}",
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );
    }
}