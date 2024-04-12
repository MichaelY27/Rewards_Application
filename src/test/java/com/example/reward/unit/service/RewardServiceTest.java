package com.example.reward.unit.service;

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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
     * calculationService.doCalculation() should be called with the correct parameters
     */
    @Test
    void testGetRewardPointsByUserId_withTransactions() {
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime currentDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionRepository.findByUserIdAndIssuedDateBetween(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        UserReward userReward = rewardService.getRewardPointsByUserId(userId);

        assertNotNull(userReward);
        verify(calculationService).doCalculation(eq(transactions), eq(startDate), eq(currentDate), eq(userReward));
    }

    /**
     * Test getRewardPointsByUserId when the transactions are empty
     * TransactionNotExistException should be thrown
     */
    @Test
    void testGetRewardPointsByUserId_withEmptyTransactions() {
        Long userId = 1L;
        when(transactionRepository.findByUserIdAndIssuedDateBetween(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(TransactionNotExistException.class, () -> rewardService.getRewardPointsByUserId(userId));
    }

    /**
     * Test getAllUserRewards
     * The result should not be null
     * The size of the result should be the same as the size of the userIds
     */
    @Test
    void testGetAllUserRewards_withUser() {
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        when(transactionRepository.findDistinctUserId()).thenReturn(userIds);
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionRepository.findByUserIdAndIssuedDateBetween(argThat(userIds::contains), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        List<UserReward> userRewardList = rewardService.getAllUserRewards();

        assertNotNull(userRewardList);
        assertEquals(userRewardList.size(), userIds.size());

    }

    /**
     * Test getAllUserRewards
     * The result should not be null
     * The size of the result should be 0
     */
    @Test
    void testGetAllUserRewards_withEmptyUser() {
        List<Long> userIds = Collections.emptyList();
        when(transactionRepository.findDistinctUserId()).thenReturn(userIds);

        List<UserReward> userRewardList = rewardService.getAllUserRewards();

        assertNotNull(userRewardList);
        assertEquals(0, userRewardList.size());
    }

}
