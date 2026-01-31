package com.minisupportdesk.common.dashboard.service;

import com.minisupportdesk.common.dashboard.DTO.TicketStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private ValueOperations<String, String> ops(){
        return redisTemplate.opsForValue();
    }

    public void increment(String key){
        ops().increment(key);
    }

    public void decrement(String key){
        ops().decrement(key);
    }

    public TicketStats getStats(){
        return new TicketStats(
                get("tickets:today"),
                get("tickets:week"),
                get("tickets:open"),
                get("tickets:resolved")
        );
    }

    public void set(String key, long value){
        try{
            ops().set(key,String.valueOf(value));
        }catch (Exception e){
            log.warn("Redis set failed for key={}", key, e);
        }
    }

    private long get(String key) {
        String val = ops().get(key);
        return val != null ? Long.parseLong(val) : 0;
    }

    public void pushStats() {
        simpMessagingTemplate.convertAndSend("/topic/admin/ticket-stats", getStats());
    }

    public void resetAll(){
        try {
            redisTemplate.delete("tickets:today");
            redisTemplate.delete("tickets:week");
            redisTemplate.delete("tickets:open");
            redisTemplate.delete("tickets:resolved");
        }catch (Exception e){
            log.warn("Redis reset failed", e);
        }
    }

}
