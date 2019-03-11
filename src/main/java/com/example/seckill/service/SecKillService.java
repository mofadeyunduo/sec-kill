package com.example.seckill.service;

import com.example.seckill.mapper.UserGoodsMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SecKillService {

    private static final String REDIS_LIST_NUMBER_KEY = "sec-kill";
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));

    @Autowired
    private RedisTemplate<String, String> redis;
    @Autowired
    private UserGoodsMapper userGoodsMapper;

    public void init(Integer number) {
        Assert.isTrue(number != null && number > 0, "number is not valid");
        List<String> nos = new LinkedList<>();
        for (Integer i = 1; i <= number; i++) {
            nos.add(i.toString());
        }
        redis.opsForList().leftPushAll(REDIS_LIST_NUMBER_KEY, nos);
    }

    public boolean secKill(Integer userId, Integer goodsId) {
        Assert.notNull(userId, "userId is not valid");
        Assert.notNull(goodsId, "goodsId is not valid");

        String no = redis.opsForList().leftPop(REDIS_LIST_NUMBER_KEY);
        if (!StringUtils.isEmpty(no)) {
            System.out.println("sec-kill userId: " + userId + " success with no: " + no);
            pool.execute(()-> userGoodsMapper.save(userId, goodsId));
            return true;
        }
        return false;
    }

}
