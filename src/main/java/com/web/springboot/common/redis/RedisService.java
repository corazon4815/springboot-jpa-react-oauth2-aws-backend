package com.web.springboot.common.redis;

import com.web.springboot.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    /**
     * redis 저장 - {id: refreshToken}
     */
    public void setValues(String id, String refreshToken){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(id, refreshToken, Duration.ofMinutes(jwtProvider.refreshTokenExpire));  // 2주뒤 메모리에서 삭제된다.
    }

    /**
     * redis 조회
     */
    public String getValues(String id){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(id);
    }

    /**
     * redis 삭제
     */
    public void delValues(String id) {
        redisTemplate.delete(id);
    }


}
