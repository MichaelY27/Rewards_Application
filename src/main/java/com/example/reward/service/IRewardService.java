package com.example.reward.service;

import com.example.reward.domain.UserReward;

import java.util.List;

public interface IRewardService {

    public UserReward getRewardPointsByUserId(Long userId);

    public List<UserReward> getAllUserRewards();
}
