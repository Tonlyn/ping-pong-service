package com.tek.pongservice.controller;

import com.tek.pongservice.service.IPongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pong")
public class PongController {

    private static final Logger logger = LoggerFactory.getLogger(PongController.class);

    @Autowired
    private IPongService pongService;

    public PongController(IPongService pongService) {
        this.pongService = pongService;
    }

    @PostMapping
    public ResponseEntity<String> pong(@RequestBody String message) {
        return pongService.pong(message);
    }

}
