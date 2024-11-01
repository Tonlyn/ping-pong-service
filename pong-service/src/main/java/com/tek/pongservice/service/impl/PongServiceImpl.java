package com.tek.pongservice.service.impl;

import com.tek.pongservice.constant.Constants;
import com.tek.pongservice.ratelimiter.AbstractRateLimiter;
import com.tek.pongservice.service.IPongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PongServiceImpl extends AbstractRateLimiter implements IPongService {

    private static final Logger logger = LoggerFactory.getLogger(PongServiceImpl.class);


    @Override
    public ResponseEntity<String> pong(String message) {
        return handleRequest();
    }

    @Override
    public ResponseEntity<String> doBusiness() {
        logger.info("[" + Thread.currentThread().getName() +  "] Handling request & Responding with 'World'");
        return ResponseEntity.ok(Constants.PONG_RESP_CONTENT);
    }
}
