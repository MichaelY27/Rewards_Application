package com.example.reward.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDTO implements Serializable {
    @NotNull(message = "Transaction id can't be null")
    @Positive(message = "Transaction id must be positive")
    @Schema(description = "Transaction id", required = true, example = "1", type = "integer")
    private Long transactionId;

    @Positive(message = "User id must be positive")
    @Schema(description = "User id", example = "1", type = "integer")
    private Long userId;


    @Positive(message = "Amount must be positive")
    @Schema(description = "Amount", example = "100.00", type = "number")
    private BigDecimal amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Issued date", example = "2024-01-01 22:05:05", pattern = "yyyy-MM-dd HH:mm:ss", type = "string")
    private LocalDateTime issuedDate;


    public UpdateTransactionDTO(long l, BigDecimal bigDecimal, LocalDateTime now) {
        this.transactionId = l;
        this.amount = bigDecimal;
        this.issuedDate = now;
    }
}
