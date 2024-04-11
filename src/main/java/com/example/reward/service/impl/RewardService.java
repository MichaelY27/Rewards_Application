package com.example.reward.service.impl;

import com.example.reward.dao.TransactionRepository;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import com.example.reward.domain.exceptions.TransactionNotExistException;
import com.example.reward.service.IRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService implements IRewardService {

    TransactionRepository transactionRepository;
    CalculationService calculationService;

    @Autowired
    public RewardService(TransactionRepository transactionRepository, CalculationService calculationService) {
        this.transactionRepository = transactionRepository;
        this.calculationService = calculationService;
    }

    @Transactional
    @Override
    public UserReward getRewardPointsByUserId(Long userId) {

        // think the first day of 3 months ago as the starting date and the first date of current month as the ending date
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDate = currentDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Transaction> transactions = transactionRepository.findByUserIdAndIssuedDateBetween(userId, startDate, currentDate);
        if (transactions.isEmpty()) {
            throw new TransactionNotExistException("No transactions found for user: " + userId + " in the last 3 months");
        }
        UserReward userReward = UserReward.builder().userId(userId).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build();
        calculationService.doCalculation(transactions, startDate, currentDate, userReward);

        return userReward;
    }
    @Transactional
    @Override
    public List<UserReward> getAllUserRewards() {
        List<Long> userIds = transactionRepository.findDistinctUserId();
        List<UserReward> userRewardList = userIds.stream().map(userId -> UserReward.builder().userId(userId).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build()).collect(Collectors.toList());
        userRewardList.forEach(userReward -> {
            LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime startDate = currentDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            List<Transaction> transactions = transactionRepository.findByUserIdAndIssuedDateBetween(userReward.getUserId(), startDate, currentDate);
            calculationService.doCalculation(transactions, startDate, currentDate, userReward);
        });
        return userRewardList;
    }
}
