package com.example.reward.unit.domain;

import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DTOTest {
    @Test
    public void testEqualsAndHashCodeCreateTransactionDTO() {
        LocalDateTime now = LocalDateTime.now();
        CreateTransactionDTO dto1 = new CreateTransactionDTO(1L, new BigDecimal("100.00"), now);
        CreateTransactionDTO dto2 = new CreateTransactionDTO(1L, new BigDecimal("100.00"), now);
        CreateTransactionDTO dto3 = new CreateTransactionDTO(2L, new BigDecimal("200.00"), now);

        assertEquals(dto1, dto1);
        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);

        if (dto1.equals(dto2) && dto2.equals(dto3)) {
            assertEquals(dto1, dto3);
        }

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
        assertNotEquals(dto1, dto3);
    }

    @Test
    public void testToStringTransactionDTO() {
        LocalDateTime now = LocalDateTime.now();
        CreateTransactionDTO dto = new CreateTransactionDTO(1L, new BigDecimal("100.00"), now);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("userId=1"));
    }

    @Test
    public void testEqualsAndHashCodeResponseDTO() {
        Instant now = Instant.now();
        ResponseDTO response1 = new ResponseDTO(now, "Error", "Details about the error");
        ResponseDTO response2 = new ResponseDTO(now, "Error", "Details about the error");
        ResponseDTO response3 = new ResponseDTO(now, "Info", "Other details");

        assertEquals(response1, response1);
        assertEquals(response1, response2);
        assertEquals(response2, response1);
        if (response1.equals(response2) && response2.equals(response3)) {
            assertEquals(response1, response3);
        }

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, null);
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, response3);
    }

    @Test
    public void testToStringResponseDTO() {
        ResponseDTO response = new ResponseDTO(Instant.now(), "Error", "Details about the error");
        assertNotNull(response.toString());
        assertTrue(response.toString().contains("Error"));
        assertTrue(response.toString().contains("Details about the error"));
    }

    @Test
    public void testEqualsAndHashCodeUpdateTransactionDTO() {
        LocalDateTime now = LocalDateTime.now();
        UpdateTransactionDTO dto1 = new UpdateTransactionDTO(1L, new BigDecimal("100.00"), now);
        UpdateTransactionDTO dto2 = new UpdateTransactionDTO(1L, new BigDecimal("100.00"), now);
        UpdateTransactionDTO dto3 = new UpdateTransactionDTO(2L, new BigDecimal("200.00"), now);

        assertEquals(dto1, dto1);
        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);

        if (dto1.equals(dto2) && dto2.equals(dto3)) {
            assertEquals(dto1, dto3);
        }

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
        assertNotEquals(dto1, dto3);
    }

    @Test
    public void testToStringUpdateTransactionDTO() {
        LocalDateTime now = LocalDateTime.now();
        UpdateTransactionDTO dto = new UpdateTransactionDTO(1L, new BigDecimal("100.00"), now);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("transactionId=1"));
        assertTrue(dto.toString().contains("amount=100.00"));
    }
}
