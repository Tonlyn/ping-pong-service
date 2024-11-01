package com.tek.pingservice.service;

import com.tek.pingservice.dto.PingRespDto;
import reactor.core.publisher.Mono;

public interface IPingService {

    Mono<PingRespDto> ping();
}