package com.example.reward.service;

import com.example.reward.domain.Transaction;
import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;

import java.util.List;

public interface ITransactionService {

    /**
     * Find a transaction by transactionId
     * @param transactionId the id of transaction
     * @return TransactionEntity
     */
    public Transaction getTransactionById(long transactionId);
    /**
     * Find all transactions belong to a user by userId
     * @param userId the id of user
     * @return List of TransactionEntity
     */
    public List<Transaction> getTransactionByUserId(long userId);
    /**
     * Create a new transaction
     * @param transactionDTO the transaction information
     * @return the generated id of the created transaction
     */
    public Long createTransaction(CreateTransactionDTO transactionDTO);
    /**
     * Update a transaction based on transaction information
     * @param transactionDTO the transaction information
     */
    public void updateTransaction(UpdateTransactionDTO transactionDTO);

    /**
     * Delete a transaction by transactionId
     * @param transactionId the id of transaction
     */
    public void deleteTransaction(Long transactionId);

    /**
     * Generate test transactions data
     */
    public void generateTestTransactions();

    /**
     * Create a list of transactions
     * @param transactionDTOList the list of transaction information
     * @return the list of generated id of the created transactions
     */
    public List<Long> createTransactionList(List<CreateTransactionDTO> transactionDTOList);


}
