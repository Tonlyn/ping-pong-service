package com.tek.pongservice.ratelimiter;

import com.tek.pongservice.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractRateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRateLimiter.class);

    private final AtomicInteger token;
    private final ConcurrentLinkedDeque<Integer> bucket;
    private final ScheduledExecutorService scheduler;

    public AbstractRateLimiter() {
        // initial bucket
        this.bucket = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < Constants.RATE_LIMIT_MAX_TOKEN; i++) {
            bucket.add(i);
        }
        token = new AtomicInteger(Constants.RATE_LIMIT_MAX_TOKEN -1);
        this.scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            if (bucket.size() < Constants.RATE_LIMIT_MAX_TOKEN) {
                int i = token.incrementAndGet();
                bucket.push(i);
                logger.info("【定时任务】发放令牌：" + i);
            }
        }, Constants.SCHEDULER_INITIAL_DELAY, Constants.SCHEDULER_PERIOD, TimeUnit.SECONDS);
    }



    public ResponseEntity<String> handleRequest() {
        // 获取可用令牌数，如果没有可用的令牌，则直接返回
        Integer value = bucket.poll();
        if (value != null) {
            // 处理请求的逻辑
            return doBusiness();
        } else {
            // 请求被丢弃
            logger.warn("[" + Thread.currentThread().getName() + "] Too Many Requests, throttling it");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        }
    }

    public abstract ResponseEntity<String> doBusiness();

}
