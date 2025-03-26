package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.kbhealthcare.ocare.healthSync.config.CacheKeys.DAILY_KEY;
import static com.kbhealthcare.ocare.healthSync.config.CacheKeys.MONTHLY_KEY;

@Service
@RequiredArgsConstructor
public class HealthSyncCacheService {
    private final RedisRepository redisRepository;

    public void evictDailyCache(String recordKey) {
        redisRepository.delete(buildDailyKey(recordKey));
    }

    public void evictMonthlyCache(String recordKey) {
        redisRepository.delete(buildMonthlyKey(recordKey));
    }

    private String buildDailyKey(String recordKey) {
        return DAILY_KEY + ":" + recordKey;
    }

    private String buildMonthlyKey(String recordKey) {
        return MONTHLY_KEY + ":" + recordKey;
    }
}
