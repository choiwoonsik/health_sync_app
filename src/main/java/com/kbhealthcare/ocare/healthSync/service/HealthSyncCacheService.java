package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return "health.sync.day:" + recordKey;
    }

    private String buildMonthlyKey(String recordKey) {
        return "health.sync.month:" + recordKey;
    }
}
