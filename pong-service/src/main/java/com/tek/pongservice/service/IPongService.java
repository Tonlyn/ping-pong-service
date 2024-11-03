package com.tek.pongservice.service;

import com.tek.pongservice.dto.PongRespDto;
import reactor.core.publisher.Mono;

/**
 * Pong Service Interface
 *
 * @author linshy
 * @date 2024/10/30
 */
public interface IPongService {

    /**
     * handle request for Ping Service
     * @param message
     * @return
     */
    Mono<PongRespDto> pong(String message);
}
