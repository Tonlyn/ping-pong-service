package com.tek.pingservice.controller;

import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.service.IPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ping")
public class PingController {

    private static final Logger logger = LoggerFactory.getLogger(PingController.class);

    @Autowired
    private IPingService pingService;

    public PingController(IPingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("")
    public Mono<PingRespDto> ping() {
        return pingService.ping();
    }
}
