package com.pc.rbac_system.service.serviceImpl;

import com.pc.rbac_system.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean setExpireTime(String key, Long second) {
        Boolean expire = false;
        if (second>0){
            expire = redisTemplate.expire(key, second, TimeUnit.SECONDS);
        }
        return expire;
    }

    @Override
    public Long getExpireTime(String key) {
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(key, TimeUnit.SECONDS);
        return expire;
    }

    @Override
    public boolean hasKey(String key) {
        Boolean aBoolean = redisTemplate.hasKey(key);
        return aBoolean;
    }

    @Override
    public <T> T get(String key) {
        T result = (T) redisTemplate.opsForValue().get(key);
        return result;
    }

    @Override
    public <T> boolean set(String key, T value) {
        if (null == key){
            redisTemplate.opsForValue().set(key,"");
            this.setExpireTime(key,5L);//数据库无相关数据时，设置5秒的数据过期时间，防止缓存穿透
        }else {
            redisTemplate.opsForValue().set(key,value);
        }
        return true;
    }

    @Override
    public boolean del(String... key) {
        if (key.length<1){
            return false;
        }
        Long delete = redisTemplate.delete(Arrays.asList(key));
        if (key.length == delete){
            return true;
        }
        return false;
    }

    @Override
    public <T> boolean setWithExpire(String key, T value, Long expire) {
        redisTemplate.opsForValue().set(key,value,expire,TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Set patternKeys(String pattern) {
        Set keys = redisTemplate.keys(pattern + "*");
        return keys;
    }
}
