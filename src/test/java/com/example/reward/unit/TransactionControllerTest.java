package com.example.reward.unit;

import com.example.reward.controller.TransactionController;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.domain.exceptions.OperationUnableToProcessException;
import com.example.reward.service.impl.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import com.example.reward.service.impl.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test createTransaction method
     * The response should be a success message
     */
    @Test
    void testCreateTransaction() throws Exception {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(1L, BigDecimal.valueOf(100), LocalDateTime.now());

        mockMvc.perform(post("/transaction/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"));
    }

    /**
     * Test updateTransaction method
     * The response should be a success message
     */
    @Test
    void testUpdateTransaction() throws Exception {
        UpdateTransactionDTO updateTransactionDTO = new UpdateTransactionDTO(1L, BigDecimal.valueOf(100), LocalDateTime.now());

        doNothing().when(transactionService).updateTransaction(updateTransactionDTO);

        mockMvc.perform(patch("/transaction/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").value("Transaction updated successfully"));
    }


    /**
     * Test getTransactionById method
     * The response should be a success message
     */
    @Test
    void testDeleteTransaction() throws Exception {
        long transactionId = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/transaction/" + transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").value("Transaction deleted successfully"));
    }




}
