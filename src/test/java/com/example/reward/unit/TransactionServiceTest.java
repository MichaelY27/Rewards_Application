package com.example.reward.unit;


import com.example.reward.dao.TransactionRepository;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import com.example.reward.domain.exceptions.OperationUnableToProcessException;
import com.example.reward.domain.exceptions.TransactionNotExistException;
import com.example.reward.service.impl.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    /**
     * Test getTransactionById when the transaction exists
     * The result should not be null
     * The result should be the same as the mock transaction
     */
    @Test
    void testGetTransactionByIdWithTransaction() {
        long transactionId = 1L;
        Transaction mockTransaction = new Transaction();
        when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(mockTransaction));

        Transaction result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(mockTransaction, result);
    }

    /**
     * Test getTransactionById when the transaction does not exist
     * TransactionNotExistException should be thrown
     */
    @Test
    void testGetTransactionByIdWithoutTransaction() {
        long transactionId = 1L;
        when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotExistException.class, () -> transactionService.getTransactionById(transactionId));
    }

    /**
     * Test createTransaction
     * The result should not be null
     * The result should be the same as the saved transaction id
     */
    @Test
    void testCreateTransactionSuccess() {
        CreateTransactionDTO dto = new CreateTransactionDTO();
        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Long resultId = transactionService.createTransaction(dto);

        assertNotNull(resultId);
        assertEquals(savedTransaction.getTransactionId(), resultId);
    }

    /**
     * Test createTransactionList
     * The result should not be null
     * The result should be the same as the saved transaction id list
     */
    @Test
    void testUpdateTransactionWhenExists() {
        UpdateTransactionDTO dto = new UpdateTransactionDTO();
        dto.setTransactionId(1L);
        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findByTransactionId(dto.getTransactionId())).thenReturn(Optional.of(existingTransaction));

        assertDoesNotThrow(() -> transactionService.updateTransaction(dto));
        verify(transactionRepository).save(any(Transaction.class));
    }

    /**
     * Test updateTransaction when the transaction does not exist
     * OperationUnableToProcessException should be thrown
     */
    @Test
    void testUpdateTransactionWhenNotExists() {
        UpdateTransactionDTO dto = new UpdateTransactionDTO();
        dto.setTransactionId(1L);
        when(transactionRepository.findByTransactionId(dto.getTransactionId())).thenReturn(Optional.empty());

        assertThrows(OperationUnableToProcessException.class, () -> transactionService.updateTransaction(dto));
    }

    /**
     * Test deleteTransaction when the transaction exists
     * The transaction should be deleted
     */
    @Test
    void testDeleteTransactionWhenExists() {
        long transactionId = 1L;
        Transaction transaction = new Transaction(); // Populate with expected properties
        when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(transaction));

        assertDoesNotThrow(() -> transactionService.deleteTransaction(transactionId));
        verify(transactionRepository).delete(transaction);
    }

    /**
     * Test deleteTransaction when the transaction does not exist
     * OperationUnableToProcessException should be thrown
     */
    @Test
    void testDeleteTransactionWhenNotExists() {
        long transactionId = 1L;
        when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        assertThrows(OperationUnableToProcessException.class, () -> transactionService.deleteTransaction(transactionId));
    }

    /**
     * Test getTransactionByUserId when the transactions exist
     * The result should not be null
     * The result should be the same as the mock transactions
     */
    @Test
    void testGetTransactionByUserIdWhenFound() {
        long userId = 1L;
        List<Transaction> expectedTransactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findTransactionsByUserId(userId)).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.getTransactionByUserId(userId);

        assertNotNull(result);
        assertEquals(expectedTransactions, result);
        verify(transactionRepository).findTransactionsByUserId(userId);
    }

    /**
     * Test getTransactionByUserId when the transactions do not exist
     * The result should not be null
     * The result should be empty
     */
    @Test
    void testGetTransactionByUserIdWhenNotFound() {
        long userId = 1L;
        when(transactionRepository.findTransactionsByUserId(userId)).thenReturn(Collections.emptyList());

        List<Transaction> result = transactionService.getTransactionByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test createTransactionList when the transactions are empty
     * The result should not be null
     * The result should be empty
     */
    @Test
    void createTransactionList_success() {
        List<CreateTransactionDTO> dtos = Arrays.asList(new CreateTransactionDTO(), new CreateTransactionDTO());
        List<Transaction> transactions = dtos.stream().map(Transaction::new).collect(Collectors.toList());
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        List<Long> ids = transactionService.createTransactionList(dtos);

        assertNotNull(ids);
        assertEquals(transactions.size(), ids.size());
        verify(transactionRepository, times(dtos.size())).save(any(Transaction.class));
    }





}
