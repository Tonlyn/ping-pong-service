package com.tek.pongservice.controller;

import com.tek.pongservice.dto.PongRespDto;
import com.tek.pongservice.service.IPongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Pong Controller to expose RESTful API
 *
 * @author linshy
 * @date 2024/10/30
 */
@RestController
@RequestMapping("/pong")
public class PongController {

    private static final Logger logger = LoggerFactory.getLogger(PongController.class);

    @Autowired
    private IPongService pongService;

    public PongController(IPongService pongService) {
        this.pongService = pongService;
    }

    /**
     * Pong RESTful API: receive request for Ping Service
     * @param message
     * @return
     */
    @PostMapping
    public Mono<PongRespDto> pong(@RequestBody String message) {
        return pongService.pong(message);
    }

}
