package com.example.reward.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ResponseDTO {
    private Instant timestamp;
    private String messageType;
    private Object message;
}
