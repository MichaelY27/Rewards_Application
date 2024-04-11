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

import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public Long createTransaction(CreateTransactionDTO transactionDTO) {
        Transaction transaction = new Transaction(transactionDTO);
        return transactionRepository.save(transaction).getTransactionId();
    }
    @Transactional
    @Override
    public List<Long> createTransactionList(List<CreateTransactionDTO> transactionDTOList) {
        return transactionDTOList.stream().map(Transaction::new).map(transactionRepository::save).map(Transaction::getTransactionId).collect(Collectors.toList());
    }


    @Transactional
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

    @Transactional
    @Override
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new OperationUnableToProcessException("Transaction with id " + transactionId + " can't be deleted since does not exist"));
        transactionRepository.delete(transaction);
    }



}
