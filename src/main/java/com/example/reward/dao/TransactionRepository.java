package com.example.reward.dao;

import com.example.reward.domain.Transaction;
import com.example.reward.domain.dto.CreateTransactionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Find a transaction by transactionId
     * @param transactionId the id of transaction
     * @return TransactionEntity
     */
    Optional<Transaction> findByTransactionId(Long transactionId);

    /**
     * Find all transactions belong to a user by userId
     * @param userId the id of user
     * @return List of TransactionEntity
     */
    List<Transaction> findTransactionsByUserId(Long userId);

    /**
     * Find all transactions belong to a user by userId and issuedAt between startDate and endDate
     * @param userId the id of user
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return List of TransactionEntity
     */
    List<Transaction> findByUserIdAndIssuedDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all transactions belong to a user by userId and issuedAt between startDate and endDate
     * @return List of TransactionEntity
     */
    @Query("SELECT DISTINCT t.userId FROM Transaction t")
    List<Long> findDistinctUserId();


}
