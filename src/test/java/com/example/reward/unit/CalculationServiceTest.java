package com.example.reward.unit;

import com.example.reward.config.RewardsConfig;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import com.example.reward.service.impl.CalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CalculationServiceTest {

    CalculationService calculationService;
    @Mock
    private RewardsConfig rewardsConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(rewardsConfig.getLevel1()).thenReturn(BigDecimal.valueOf(50));
        when(rewardsConfig.getLevel1Points()).thenReturn(BigDecimal.valueOf(1));
        when(rewardsConfig.getLevel2()).thenReturn(BigDecimal.valueOf(100));
        when(rewardsConfig.getLevel2Points()).thenReturn(BigDecimal.valueOf(2));
        calculationService = new CalculationService(rewardsConfig);
    }
    @Test
    public void testCalculateRewardsPoints() {
        int[] testAmounts = {0, 50, 100, 120, 150, 200};
        int[] expectedTestResults = {0, 0, 50, 90, 150, 250};
        for (int i = 0; i < testAmounts.length; i++) {
            Transaction transaction = new Transaction(1L, BigDecimal.valueOf(testAmounts[i]), LocalDateTime.now());
            Integer testResult = calculationService.calculateRewardPoints(transaction);
            assertEquals(expectedTestResults[i], testResult);
        }
    }

    @Test
    public void testDoCalculation() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, new BigDecimal(120), LocalDateTime.now().minusMonths(1).withDayOfMonth(1)),
                new Transaction(1L, new BigDecimal(100),LocalDateTime.now().minusMonths(2).withDayOfMonth(1)),
                new Transaction(1L, new BigDecimal(70), LocalDateTime.now().minusMonths(3).withDayOfMonth(1))
        );
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDate = currentDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        UserReward userReward = UserReward.builder().userId(1L).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build();

        calculationService.doCalculation(transactions, startDate, currentDate, userReward);

        assertEquals(90, userReward.getLastMonthPoints());
        assertEquals(50, userReward.getTwoMonthsAgoPoints());
        assertEquals(20, userReward.getThreeMonthsAgoPoints());
        assertEquals(160, userReward.getTotalPoints(), "Total points should sum up correctly.");
    }

    @Test
    public void testDoCalculationWithNoTransactions() {
        List<Transaction> transactions = Arrays.asList();
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDate = currentDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        UserReward userReward = UserReward.builder().userId(1L).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build();

        calculationService.doCalculation(transactions, startDate, currentDate, userReward);

        assertEquals(0, userReward.getLastMonthPoints());
        assertEquals(0, userReward.getTwoMonthsAgoPoints());
        assertEquals(0, userReward.getThreeMonthsAgoPoints());
        assertEquals(0, userReward.getTotalPoints(), "Total points should sum up correctly.");
    }

    @Test
    public void testDoCalculationWithNoTransactionsPastThreeMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, new BigDecimal(120), LocalDateTime.now().minusMonths(4).withDayOfMonth(1)),
                new Transaction(1L, new BigDecimal(100), LocalDateTime.now().minusMonths(4).withDayOfMonth(1)),
                new Transaction(1L, new BigDecimal(70), LocalDateTime.now().minusMonths(4).withDayOfMonth(1))
        );
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startDate = currentDate.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        UserReward userReward = UserReward.builder().userId(1L).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build();

        calculationService.doCalculation(transactions, startDate, currentDate, userReward);

        assertEquals(0, userReward.getLastMonthPoints());
        assertEquals(0, userReward.getTwoMonthsAgoPoints());
        assertEquals(0, userReward.getThreeMonthsAgoPoints());
        assertEquals(0, userReward.getTotalPoints(), "Total points should sum up correctly.");
    }



}
