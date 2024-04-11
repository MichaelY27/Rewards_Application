package com.example.reward.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "rewards")
@Getter
@Setter
public class RewardsConfig {

    @Value("${rewards.threshold.level1}")
    private BigDecimal level1;
    @Value("${rewards.threshold.level2}")
    private BigDecimal level2;
    @Value("${rewards.points.level1}")
    private BigDecimal level1Points;
    @Value("${rewards.points.level2}")
    private BigDecimal level2Points;

}
