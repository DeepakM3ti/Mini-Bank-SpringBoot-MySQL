package com.example.minibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    private Long id;

    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Enter a valid 10 digit phone number"
    )
    private String phone;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Balance cannot be negative"
    )
    private BigDecimal balance;

    @NotBlank(message = "Account type cannot be blank")
    private String accountType;
}