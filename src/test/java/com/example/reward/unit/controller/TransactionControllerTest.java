package com.example.reward.unit.controller;

import com.example.reward.controller.TransactionController;
import com.example.reward.domain.Transaction;
import com.example.reward.service.impl.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
     * Test getTransactionById method
     * The response should be a success message
     */
    @Test
    void testGetTransactionById() throws Exception {
        long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, 1L, BigDecimal.valueOf(100), LocalDateTime.now());
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        mockMvc.perform(get("/transaction/" + transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message.transactionId").value(1))
                .andExpect(jsonPath("$.message.userId").value(1));
    }

    /**
     * Test getAllTransactions method
     * The response should be a success message
     */
    @Test
    void testGetAllTransactions() throws Exception {
        long userId = 1L;
        List<Transaction> transactionList = Arrays.asList(new Transaction(1L, userId, BigDecimal.valueOf(100), LocalDateTime.now()), new Transaction(2L, userId, BigDecimal.valueOf(100), LocalDateTime.now()));
        when(transactionService.getTransactionByUserId(userId)).thenReturn(transactionList);

        mockMvc.perform(get("/transaction/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message[0].transactionId").value(1))
                .andExpect(jsonPath("$.message[1].transactionId").value(2));
    }

    /**
     * Test createTransaction method
     * The response should be a success message
     */
    @Test
    void testCreateTransactionSingle() throws Exception {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(1L, BigDecimal.valueOf(100), LocalDateTime.now());

        mockMvc.perform(post("/transaction/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"));
    }

    /**
     * Test createTransaction method
     * The request is valid, the response should be a success message
     */
    @Test
    void testCreateTransactionMany_success() throws Exception {
        List<CreateTransactionDTO> createTransactionDTOList = Arrays.asList(new CreateTransactionDTO(1L, BigDecimal.valueOf(100), LocalDateTime.now()), new CreateTransactionDTO(2L, BigDecimal.valueOf(100), LocalDateTime.now()));
        List<Long> transactionIdList = Arrays.asList(1L, 2L);
        Mockito.when(transactionService.createTransactionList(any())).thenReturn(transactionIdList);
        mockMvc.perform(post("/transaction/many")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionDTOList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").value("Transaction list created successfully"));
    }

    /**
     * Test createTransaction method
     * The request is invalid, the response should be a failure message
     */
    @Test
    void testCreateTransactionMany_failure() throws Exception {
        List<CreateTransactionDTO> createTransactionDTOList = Collections.emptyList();
        Mockito.when(transactionService.createTransactionList(any())).thenThrow(new RuntimeException("Transaction creation failed"));
        mockMvc.perform(post("/transaction/many")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionDTOList)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageType").value("Error"))
                .andExpect(jsonPath("$.message").value("createTransactionMany.createTransactionDTOList: transaction list can't be empty"));

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
