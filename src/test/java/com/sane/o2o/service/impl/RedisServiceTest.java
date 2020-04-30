package com.sane.o2o.service.impl;

import com.sane.o2o.BaseTest;
import com.sane.o2o.service.RedisService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisServiceTest extends BaseTest {
    @Autowired
    private RedisService redisService;
    private Logger logger= LoggerFactory.getLogger(RedisServiceTest.class);
    @Test
    public void redisTest(){
       Boolean result=redisService.set("key",2);
        if(result){
            logger.info("dddddddd");
            System.out.println(redisService.get("key"));
        }
    }
}
