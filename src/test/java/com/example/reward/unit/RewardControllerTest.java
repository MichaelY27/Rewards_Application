package com.example.reward.unit;

import com.example.reward.controller.RewardController;
import com.example.reward.domain.UserReward;
import com.example.reward.service.impl.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RewardController(rewardService)).build();
    }

    /**
     * Test getRewardPoints method
     * The response should be a success message
     * The response should contain a UserReward object
     */
    @Test
    public void testGetRewardPoints() throws Exception {
        Long userId = 1L;
        UserReward userReward = UserReward.builder().userId(userId).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build();
        when(rewardService.getRewardPointsByUserId(userId)).thenReturn(userReward);

        mockMvc.perform(get("/reward/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").isNotEmpty());

        verify(rewardService).getRewardPointsByUserId(userId);
    }

    /**
     * Test getRewardPointsSummary method
     * The response should be a success message
     * The response should contain a list of UserReward objects
     */
    @Test
    public void testGetRewardPointsSummary() throws Exception {
        List<UserReward> userRewards = Arrays.asList(UserReward.builder().userId(1L).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build(), UserReward.builder().userId(2L).lastMonthPoints(0).twoMonthsAgoPoints(0).threeMonthsAgoPoints(0).totalPoints(0).build());
        when(rewardService.getAllUserRewards()).thenReturn(userRewards);

        mockMvc.perform(get("/reward/summary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messageType").value("Success"))
                .andExpect(jsonPath("$.message").isArray())
                .andExpect(jsonPath("$.message.length()").value(userRewards.size()));

        verify(rewardService).getAllUserRewards();
    }
}
