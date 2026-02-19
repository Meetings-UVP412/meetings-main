package demo.meetingsmain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveAudio(String key, byte[] audioData) {
        redisTemplate.opsForValue().set(key, audioData);
    }

    public byte[] getAudio(String key) {
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    public void deleteAudio(String key) {
        redisTemplate.delete(key);
    }
}
