package com.example.reward.service.impl;

import com.example.reward.config.RewardsConfig;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import com.example.reward.domain.exceptions.IllegalTransactionException;
import com.example.reward.service.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalculationService implements ICalculationService {

    RewardsConfig rewardsConfig;
    @Autowired
    public CalculationService(RewardsConfig rewardsConfig) {
        this.rewardsConfig = rewardsConfig;
    }

    /**
     * Calculate reward points for each transaction and sum up the points for each month
     * @param transactions list of transactions
     * @param startDate start date of three months ago
     * @param endDate end date of current month
     * @param userReward user reward object to store the points
     */
    @Override
    public void doCalculation(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate, UserReward userReward) {

        LocalDateTime lastMonthStartDate = endDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastTwoMonthStartDate = endDate.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastThreeMonthStartDate = endDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        transactions.forEach(transaction -> {
            // last month
            if (transaction.getIssuedDate().isAfter(lastMonthStartDate) && transaction.getIssuedDate().isBefore(endDate)) {
                userReward.setLastMonthPoints(userReward.getLastMonthPoints() + calculateRewardPoints(transaction));
            }
            // two months ago
            if (transaction.getIssuedDate().isAfter(lastTwoMonthStartDate) && transaction.getIssuedDate().isBefore(lastMonthStartDate)) {
                userReward.setTwoMonthsAgoPoints(userReward.getTwoMonthsAgoPoints() + calculateRewardPoints(transaction));
            }
            // three months ago
            if (transaction.getIssuedDate().isAfter(lastThreeMonthStartDate) && transaction.getIssuedDate().isBefore(lastTwoMonthStartDate)) {
                userReward.setThreeMonthsAgoPoints(userReward.getThreeMonthsAgoPoints() + calculateRewardPoints(transaction));
            }
        });
        userReward.setTotalPoints(userReward.getLastMonthPoints() + userReward.getTwoMonthsAgoPoints() + userReward.getThreeMonthsAgoPoints());
    }

    /**
     * Calculate reward points for each transaction
     * @param transaction transaction object
     * @return reward points
     */
    public Integer calculateRewardPoints(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalTransactionException("Transaction amount cannot be negative");
        }
        BigDecimal total = transaction.getAmount();
        BigDecimal points = BigDecimal.valueOf(0);
        if (total.compareTo(rewardsConfig.getLevel2()) > 0) {
            BigDecimal second = total.subtract(rewardsConfig.getLevel2()).multiply(rewardsConfig.getLevel2Points());
            points = points.add(second);
            total = rewardsConfig.getLevel2();
        }
        if (total.compareTo(rewardsConfig.getLevel1()) > 0) {
            BigDecimal first = total.subtract(rewardsConfig.getLevel1()).multiply(rewardsConfig.getLevel1Points());
            points = points.add(first);
        }
        if (points.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new IllegalTransactionException("Points exceed the maximum value");
        }
        return points.intValue();
    }
}
