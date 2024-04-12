package com.example.reward.unit.domain;

import com.example.reward.domain.Transaction;
import com.example.reward.domain.UserReward;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DomainTest {

    @Test
    public void testEqualsAndHashTransaction() {
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction1 = new Transaction(1L, new BigDecimal("100.00"), now);
        Transaction transaction2 = new Transaction(1L, new BigDecimal("100.00"), now);
        Transaction transaction3 = new Transaction(2L, new BigDecimal("200.00"), now);

        assertEquals(transaction1, transaction1);
        assertEquals(transaction1, transaction2);
        assertEquals(transaction2, transaction1);

        if (transaction1.equals(transaction2) && transaction2.equals(transaction3)) {
            assertEquals(transaction1, transaction3);
        }

        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, new Object());
        assertNotEquals(transaction1, transaction3);
    }

    @Test
    public void testToStringTransaction() {
        Transaction transaction = new Transaction(1L, new BigDecimal("100.00"), LocalDateTime.now());
        String result = transaction.toString();

        assertNotNull(result);
        assertTrue(result.contains("userId=1"));
        assertTrue(result.contains("amount=100.00"));
    }

    @Test
    public void testEqualsAndHashCodeReward() {
        UserReward reward1 = new UserReward(1L, 100, 90, 80, 70);
        UserReward reward2 = new UserReward(1L, 100, 90, 80, 70);
        UserReward reward3 = new UserReward(2L, 200, 190, 180, 170);

        assertEquals(reward1, reward1);
        assertEquals(reward1, reward2);
        assertEquals(reward2, reward1);

        if (reward1.equals(reward2) && reward2.equals(reward3)) {
            assertEquals(reward1, reward3);
        }

        assertEquals(reward1, reward2);
        assertEquals(reward1.hashCode(), reward2.hashCode());
        assertNotEquals(reward1, null);
        assertNotEquals(reward1, new Object());
        assertNotEquals(reward1, reward3);
    }

    @Test
    public void testToStringReward() {
        UserReward reward = new UserReward(1L, 100, 90, 80, 70);
        assertNotNull(reward.toString());
        assertTrue(reward.toString().contains("userId=1"));
    }
}
