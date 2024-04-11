package com.example.reward.unit;

import com.example.reward.dao.TransactionRepository;
import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import com.example.reward.domain.exceptions.TransactionNotExistException;
import com.example.reward.service.impl.CalculationService;
import com.example.reward.service.impl.RewardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private RewardService rewardService;

    /**
     * Test getRewardPointsByUserId when the transactions are not empty
     * userReward should not be null
     * calculationService.doCalculation should be called
     */
    @Test
    void testGetRewardPointsByUserIdWithTransactions() {
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

    /**
     * Test getRewardPointsByUserId when the transactions are empty
     * TransactionNotExistException should be thrown
     */
    @Test
    void testGetRewardPointsByUserIdWithEmptyTransactions() {
        Long userId = 1L;
        when(transactionRepository.findByUserIdAndIssuedDateBetween(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(TransactionNotExistException.class, () -> rewardService.getRewardPointsByUserId(userId));
    }

}
