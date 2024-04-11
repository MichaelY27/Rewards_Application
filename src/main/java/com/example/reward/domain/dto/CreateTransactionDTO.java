package com.example.reward.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTransactionDTO implements Serializable {
    @NotNull(message = "User id can't be null")
    @Positive(message = "User id must be positive")
    @Schema(description = "User id", required = true, example = "1", type = "integer")
    private Long userId;

    @NotNull(message = "Amount can't be null")
    @Positive(message = "Amount must be positive")
    @Schema(description = "Amount", required = true, example = "100.00", type = "number")
    private BigDecimal amount;

    @NotNull(message = "Issued date can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Issued date", required = true, example = "2024-01-01 22:05:05", pattern = "yyyy-MM-dd HH:mm:ss", type = "string")
    private LocalDateTime issuedDate;

}
