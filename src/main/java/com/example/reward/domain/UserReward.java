package com.example.reward.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserReward {
    private Long userId;
    private Integer totalPoints;
    private Integer lastMonthPoints;
    private Integer twoMonthsAgoPoints;
    private Integer threeMonthsAgoPoints;
}
