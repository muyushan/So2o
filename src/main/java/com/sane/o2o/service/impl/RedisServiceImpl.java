package com.sane.o2o.service.impl;

import com.sane.o2o.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    private  static  final Logger logger= LoggerFactory.getLogger(RedisServiceImpl.class);
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设定缓存失效时间
     * @param key 键
     * @param time 时间（秒）
     * @return
     */
    @Override
    public boolean expire(String key, long time) {
        if(time>0){
            try{

                    redisTemplate.expire(key,time, TimeUnit.SECONDS);
                    return true;

            }catch (Exception ex){
                log(ex);
                return false;
            }
        }else{
            return false;
        }

    }

    /**
     * 根据key获取值的失效时间
     * @param key
     * @return 时间（秒）返回0代表永久有效
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存 可以传入多个key
     * @param key
     */
    @Override
    public void del(String... key) {
        if(key!=null&&key.length>0){
        if(key.length==1){
            redisTemplate.delete(key[0]);
        }else{
            redisTemplate.delete(CollectionUtils.arrayToList(key));
        }
        }
    }
    //================================String 相关操作=========================================
    /**
     *根据key获取缓存值
     * @param key
     * @return
     */
    @Override
    public Object get(String key) {
        return  redisTemplate.opsForValue().get(key);

    }

    /**
     * 缓存存入
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, Object value) {
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception ex){
            log(ex);
            return false;
        }


    }

    /**
     * 缓存放入，并设置过期时间
     * @param key
     * @param value
     * @param time
     * @return
     */

    @Override
    public boolean set(String key, Object value, long time) {
        if(time>0){
            try{
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
                return true;
            }catch (Exception ex){
                log(ex);
                return  false;
            }
        }else{
            return set(key,value);
        }
    }

    /**
     * 递增
     * @param key
     * @param delta 要增加的因子（大于等于0）
     * @return
     * @throws Exception
     */
    @Override
    public long incr(String key, long delta) throws Exception {
        if(delta<0){
            throw new Exception("增长因子不能小于0");
        }
        return  redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 递减
     * @param key
     * @param delta 要减少的因子（大于等于0）
     * @return
     * @throws Exception
     */
    @Override
    public long decr(String key, long delta) throws Exception{
        if(delta<0){
            throw new Exception("递减因子必须大于0");
        }
        return  redisTemplate.opsForValue().increment(key,-delta);
    }

    //================================Hash相关操作=========================================

    /**
     *Hash GET
     * @param key
     * @param item
     * @return
     */
    @Override
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key,item);
    }

    /**
     * 获取hash key 对应的所有键 值
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> hmget(String key) {
       return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Hash set
     * @param key
     * @param map 对应多个键值
     * @return
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        try{
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception ex){
            log(ex);
            return  false;
        }
    }

    /**
     * Hash set 并设置过期时间
     * @param key
     * @param map
     * @param time 时间必须大于0 单位为秒 如果时间小于哦则为长期有效
     * @return
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        boolean result=hmset(key,map);
        if(time>0&&result){
            expire(key, time);
            return  true;
        }
        return result;
    }

    /**
     * 存入或更新指定键 项 的值
     * @param key
     * @param item
     * @param value
     * @return
     */
    @Override
    public boolean hset(String key, String item, Object value) {
        try{
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception ex){
            log(ex);
            return false;
        }
    }

    /**
     * 存入或更新指定键 项 的值 并设置超时时间，单位秒
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    @Override
    public boolean hset(String key, String item, Object value, long time) {
      try{
          hset(key, item, value);
          if(time>0){
              expire(key,time);
          }
          return true;
      }catch (Exception ex){
          log(ex);
          return false;
      }
    }

    /**
     * 删除hash 表中的缓存
     * @param key
     * @param item
     */
    @Override
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断 key对应的hash表中是否存在 item
     * @param key
     * @param item
     * @return
     */
    @Override
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key,item);
    }

    /**
     * hash item 递增
     * @param key
     * @param item
     * @param by
     * @return
     * @throws Exception
     */
    @Override
    public double hincr(String key, String item, double by) throws Exception{
        if(by<0){
            throw new Exception("增长因子必须大于0");
        }
       return redisTemplate.opsForHash().increment(key,item,by);
    }

    /**
     * hash item 递减
     * @param key
     * @param item
     * @param by
     * @return
     * @throws Exception
     */
    @Override
    public double hdecr(String key, String item, double by)throws Exception{
        if(by<0){
            throw new Exception("增长因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key,item,-by);
    }

    //================================Set相关操作=========================================

    /**
     * 根据key获取set 中的所有值
     * @param key
     * @return
     */
    @Override
    public Set<Object> sGet(String key) {
        try{
            return redisTemplate.opsForSet().members(key);
        }catch (Exception ex){
            log(ex);
            return  null;
        }
    }

    /**
     * 判断set是否包含
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key,value);
    }

    /**
     * 将数据存入set
     * @param key
     * @param values
     * @return
     */
    @Override
    public long sSet(String key, Object... values) {
       return redisTemplate.opsForSet().add(key,values);

    }

    /**
     * 将数据存入set并设置超期时间
     * @param key
     * @param time
     * @param values
     * @return
     */
    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        long count=sSet(key, values);
        if(time>0){
            expire(key, time);
        }
        return count;
    }

    /**
     * 获取指定key 的set的元素个数
     * @param key
     * @return
     */
    @Override
    public long sGetSetSize(String key) {
        return  redisTemplate.opsForSet().size(key);
    }

    /**
     * 删除指定key的set 元素
     * @param key
     * @param values
     * @return
     */
    @Override
    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key,values);
    }

    //=============================list 相关操作===============================

    /**
     * 获取list缓存的内容
     * @param key
     * @param start
     * @param end 0 或-1代表到结尾
     * @return
     */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 获取list size
     * @param key
     * @return
     */
    @Override
    public long lGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取指定索引的值
     * @param key
     * @param index
     * @return
     */
    @Override
    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key,index);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean lSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key,value);
        return true;
    }

    @Override
    public boolean lSet(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key,value);
        if(time>0){
            expire(key,time);
        }
        return true;
    }

    @Override
    public boolean lSet(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushAll(key,value);
        return true;
    }

    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        redisTemplate.opsForList().rightPushAll(key,value);
        if(time>0){
            expire(key,time);
        }
        return true;
    }

    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key,index,value);
        return true;
    }

    /**
     * 移除count个值为value的元素
     * @param key
     * @param count
     * @param value
     * @return
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        return  redisTemplate.opsForList().remove(key,count,value);
    }

    //===============================sorted set 相关操作==========================================

    /**
     * 向有序集合添加一个成员
     * @param key
     * @param member
     * @param score
     * @param time
     * @return
     */
    @Override
    public boolean zadd(String key, Object member, double score, long time) {
        redisTemplate.opsForZSet().add(key,member,score);
        if(time>0){
            expire(key,time);
        }
        return true;
    }

    /**
     * 	通过分数返回有序集合指定区间内的成员
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    @Override
    public Set<Object> zRangeByScore(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key,minScore,maxScore);
    }

    /**
     * 返回有序集中，成员的分数值
     * @param key
     * @param member
     * @return
     */
    @Override
    public Double zscore(String key, Object member) {
        return redisTemplate.opsForZSet().score(key,member);
    }

    /**
     * 返回有序集合中指定成员的索引
     * @param key
     * @param member
     * @return
     */
    @Override
    public Long zrank(String key, Object member) {
        return  redisTemplate.opsForZSet().rank(key,member);
    }

    /**
     * Zscan 迭代有序集合中的元素（包括元素成员和元素分值）
     * @param key
     * @return
     */
    @Override
    public Cursor<ZSetOperations.TypedTuple<Object>> zscan(String key) {
        Cursor<ZSetOperations.TypedTuple<Object>> cursor=redisTemplate.opsForZSet().scan(key, ScanOptions.NONE);
        return cursor;
    }

    private void log(Exception ex){
        logger.error("redis error:"+ex.getMessage());
    }
}
