package com.example.reward.service;

import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;

import java.time.LocalDateTime;
import java.util.List;

public interface ICalculationService {

    public void doCalculation(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate, UserReward userReward);
}
