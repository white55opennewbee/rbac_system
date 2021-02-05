package com.pc.rbac_system.service;

import java.util.List;
import java.util.Set;

public interface IRedisService {
    boolean setExpireTime(String key, Long second);
    Long getExpireTime(String key);
    boolean hasKey(String key);
    <T> T get(String key);
    <T> boolean set(String key, T value);
    boolean del(String... key);
    <T> boolean setWithExpire(String key ,T value,Long expire);
    Set patternKeys(String pattern);
}
