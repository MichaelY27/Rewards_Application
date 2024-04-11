package com.example.reward.domain;

import com.example.reward.domain.dto.CreateTransactionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "User id can't be null")
    @Positive(message = "User id must be positive")
    private Long userId;

    @NotNull(message = "Amount can't be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Issued date can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuedDate;

    public Transaction(CreateTransactionDTO createTransactionDTO) {
        this.userId = createTransactionDTO.getUserId();
        this.amount = createTransactionDTO.getAmount();
        this.issuedDate = createTransactionDTO.getIssuedDate();
    }
    public Transaction(Long userId, BigDecimal amount, LocalDateTime issuedDate) {
        this.userId = userId;
        this.amount = amount;
        this.issuedDate = issuedDate;
    }
}
