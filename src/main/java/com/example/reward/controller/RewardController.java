package com.example.reward.controller;

import com.example.reward.domain.UserReward;
import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.service.impl.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Validated
@RestController
@RequestMapping("/reward")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @Operation(summary = "Get reward points by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reward points retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> getRewardPoints(@PathVariable @NotNull(message = "User id can't be null") Long userId){
        UserReward userReward = rewardService.getRewardPointsByUserId(userId);
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message(userReward).build(), HttpStatus.OK);
    }


    @Operation(summary = "Get reward points summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reward points retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/summary")
    public ResponseEntity<ResponseDTO> getRewardPointsSummary(){
        List<UserReward> userRewardList = rewardService.getAllUserRewards();
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message(userRewardList).build(), HttpStatus.OK);
    }
}
