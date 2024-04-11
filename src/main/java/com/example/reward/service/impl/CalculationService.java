package com.example.reward.service.impl;

import com.example.reward.config.RewardsConfig;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
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

    @Override
    public void doCalculation(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate, UserReward userReward) {

        LocalDateTime lastMonthStartDate = endDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastTwoMonthStartDate = endDate.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastThreeMonthStartDate = endDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        transactions.forEach(transaction -> {
            if (transaction.getIssuedDate().isAfter(lastMonthStartDate) && transaction.getIssuedDate().isBefore(endDate)) {
                userReward.setLastMonthPoints(userReward.getLastMonthPoints() + calculateRewardPoints(transaction));
            } else if (transaction.getIssuedDate().isAfter(lastTwoMonthStartDate) && transaction.getIssuedDate().isBefore(lastMonthStartDate)) {
                userReward.setTwoMonthsAgoPoints(userReward.getTwoMonthsAgoPoints() + calculateRewardPoints(transaction));
            } else if (transaction.getIssuedDate().isAfter(lastThreeMonthStartDate) && transaction.getIssuedDate().isBefore(lastTwoMonthStartDate)) {
                userReward.setThreeMonthsAgoPoints(userReward.getThreeMonthsAgoPoints() + calculateRewardPoints(transaction));
            }
        });
        userReward.setTotalPoints(userReward.getLastMonthPoints() + userReward.getTwoMonthsAgoPoints() + userReward.getThreeMonthsAgoPoints());
    }

    public Integer calculateRewardPoints(Transaction transaction) {
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
        return points.intValue();
    }
}
