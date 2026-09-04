package com.example.minibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotBlank(message = "From account cannot be blank")
    private String fromAccount;

    @NotBlank(message = "To account cannot be blank")
    private String toAccount;

    @DecimalMin(
            value = "0.01",
            message = "Transfer amount must be greater than zero"
    )
    private BigDecimal amount;
}