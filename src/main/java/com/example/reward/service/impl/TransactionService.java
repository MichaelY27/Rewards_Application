package com.example.reward.service.impl;

import com.example.reward.dao.TransactionRepository;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import com.example.reward.domain.exceptions.OperationUnableToProcessException;
import com.example.reward.domain.exceptions.TransactionNotExistException;
import com.example.reward.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction getTransactionById(long transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotExistException("Transaction with id " + transactionId + " does not exist"));
    }

    @Override
    public List<Transaction> getTransactionByUserId(long userId) {
        return transactionRepository.findTransactionsByUserId(userId);
    }

    @Override
    public Long createTransaction(CreateTransactionDTO transactionDTO) {
        Transaction transaction = new Transaction(transactionDTO);
        return transactionRepository.save(transaction).getTransactionId();
    }

    @Override
    public List<Long> createTransactionList(List<CreateTransactionDTO> transactionDTOList) {
        return transactionDTOList.stream().map(Transaction::new).map(transactionRepository::save).map(Transaction::getTransactionId).collect(Collectors.toList());
    }

    /**
     * Update or Partially update a transaction based on transaction information
     * @param transactionDTO the transaction information
     */
    @Override
    public void updateTransaction(UpdateTransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionDTO.getTransactionId())
                .orElseThrow(() -> new OperationUnableToProcessException("Transaction with id " + transactionDTO.getTransactionId() + " can't be modified since it does not exist"));
        setTransactionProperties(transaction, transactionDTO);
        transactionRepository.save(transaction);
    }
    private void setTransactionProperties(Transaction transaction, UpdateTransactionDTO transactionDTO) {
        if (transactionDTO.getAmount() != null) {
            transaction.setAmount(transactionDTO.getAmount());
        }
        if (transactionDTO.getUserId() != null) {
            transaction.setUserId(transactionDTO.getUserId());
        }
        if (transactionDTO.getIssuedDate() != null) {
            transaction.setIssuedDate(transactionDTO.getIssuedDate());
        }
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new OperationUnableToProcessException("Transaction with id " + transactionId + " can't be deleted since does not exist"));
        transactionRepository.delete(transaction);
    }

    @Override
    public void generateTestTransactions() {
        // User 1
        // last month, user 1, 3 transactions: 100/150/200 $ respectively
        transactionRepository.save(new Transaction(1L, BigDecimal.valueOf(120), LocalDateTime.now().minusMonths(1)));
        transactionRepository.save(new Transaction(1L, BigDecimal.valueOf(150), LocalDateTime.now().minusMonths(1)));
        transactionRepository.save(new Transaction(1L, BigDecimal.valueOf(200), LocalDateTime.now().minusMonths(1)));
        // two months ago, user 1, 1 transactions: 100 $
        transactionRepository.save(new Transaction(1L, BigDecimal.valueOf(100), LocalDateTime.now().minusMonths(2)));
        // three months ago, user 1, 1 transactions: 200 $
        transactionRepository.save(new Transaction(1L, BigDecimal.valueOf(200), LocalDateTime.now().minusMonths(3)));

        // User 2
        // last month, user 2, 2 transactions: 80/150 $ respectively
        transactionRepository.save(new Transaction(2L, BigDecimal.valueOf(80), LocalDateTime.now().minusMonths(1)));
        transactionRepository.save(new Transaction(2L, BigDecimal.valueOf(150), LocalDateTime.now().minusMonths(1)));
        // two months ago, user 2, 1 transactions: 300 $
        transactionRepository.save(new Transaction(2L, BigDecimal.valueOf(100), LocalDateTime.now().minusMonths(2)));

        // User 3
        // three months ago, user 3, 1 transactions: 300 $
        transactionRepository.save(new Transaction(3L, BigDecimal.valueOf(300), LocalDateTime.now().minusMonths(3)));

    }


}
