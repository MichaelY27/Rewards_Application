package com.example.reward.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReward {
    private Long userId;
    private Integer totalPoints;
    private Integer lastMonthPoints;
    private Integer twoMonthsAgoPoints;
    private Integer threeMonthsAgoPoints;
}
