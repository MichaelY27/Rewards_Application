package com.example.reward.unit;

import com.example.reward.dao.TransactionRepository;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import com.example.reward.service.impl.CalculationService;
import com.example.reward.service.impl.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void getRewardPointsByUserId_withTransactions_shouldReturnUserReward() {
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Transaction> transactions = Collections.singletonList(new Transaction());

        when(transactionRepository.findByUserIdAndIssuedDateBetween(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        UserReward userReward = rewardService.getRewardPointsByUserId(userId);

        assertNotNull(userReward);
        verify(calculationService).doCalculation(eq(transactions), eq(startDate), eq(currentDate), any(UserReward.class));
    }

}
