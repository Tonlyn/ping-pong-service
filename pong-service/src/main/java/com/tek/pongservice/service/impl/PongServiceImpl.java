package com.tek.pongservice.service.impl;

import com.tek.pongservice.constant.Constants;
import com.tek.pongservice.dto.PongRespDto;
import com.tek.pongservice.ratelimiter.AbstractRateLimiter;
import com.tek.pongservice.service.IPongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Pong Service Implement class
 *
 * @author linshy
 * @date 2024/10/30
 */
@Service
public class PongServiceImpl extends AbstractRateLimiter implements IPongService {

    private static final Logger logger = LoggerFactory.getLogger(PongServiceImpl.class);


    @Override
    public Mono<PongRespDto> pong(String message) {
        return handleRequest();
    }

    @Override
    public Mono<PongRespDto> doBusiness() {
        logger.info("[{}] Handling request & Responding with 'World'", Thread.currentThread().getName());
        return Mono.just(new PongRespDto("" + HttpStatus.OK.value(), Constants.PONG_RESP_CONTENT));
    }
}
