package com.tek.pingservice.service;

import com.tek.pingservice.dto.PingRespDto;
import reactor.core.publisher.Mono;

/**
 * Ping Service Interface
 *
 * @author linshy
 * @date 2024/10/30
 */
public interface IPingService {

    /**
     * Ping Service: say Hello to Pong Service
     * @return the result from Poong Service
     */
    Mono<PingRespDto> ping();
}