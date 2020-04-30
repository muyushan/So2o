package com.sane.o2o.service;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    public boolean expire(String key, long time);
    public long getExpire(String key);
    public boolean hasKey(String key);
    public void del(String... key);
    public Object get(String key);
    public boolean set(String key, Object value);
    public boolean set(String key, Object value, long time);
    public long incr(String key, long delta) throws Exception;
    public long decr(String key, long delta) throws Exception;
    public Object hget(String key, String item);
    public Map<Object, Object> hmget(String key);
    public boolean hmset(String key, Map<String, Object> map);
    public boolean hmset(String key, Map<String, Object> map, long time);
    public boolean hset(String key, String item, Object value);
    public boolean hset(String key, String item, Object value, long time);
    public void hdel(String key, Object... item);
    public boolean hHasKey(String key, String item);
    public double hincr(String key, String item, double by) throws Exception;
    public double hdecr(String key, String item, double by) throws Exception;
    public Set<Object> sGet(String key);
    public boolean sHasKey(String key, Object value);
    public long sSet(String key, Object... values);
    public long sSetAndTime(String key, long time, Object... values);
    public long sGetSetSize(String key);
    public long setRemove(String key, Object... values);
    public List<Object> lGet(String key, long start, long end);
    public long lGetListSize(String key);
    public Object lGetIndex(String key, long index);
    public boolean lSet(String key, Object value);
    public boolean lSet(String key, Object value, long time);
    public boolean lSet(String key, List<Object> value);
    public boolean lSet(String key, List<Object> value, long time);
    public boolean lUpdateIndex(String key, long index, Object value);
    public long lRemove(String key, long count, Object value);
    public boolean zadd(String key, Object member, double score, long time);
    public Set<Object> zRangeByScore(String key, double minScore, double maxScore);
    public Double zscore(String key, Object member);
    public Long zrank(String key, Object member);
    public Cursor<ZSetOperations.TypedTuple<Object>> zscan(String key);

}
