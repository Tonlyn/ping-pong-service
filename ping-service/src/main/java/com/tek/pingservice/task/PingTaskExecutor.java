package com.tek.pingservice.task;

import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.service.IPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PingTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(PingTaskExecutor.class);

    @Autowired
    private IPingService pingService;

    public PingTaskExecutor(IPingService pingService) {
        this.pingService = pingService;
    }

    //@Scheduled(fixedRate = 500, initialDelay = 5000)
    @Scheduled(cron = "0/1 * * * * ? ")
    public void execute(){
        Mono<PingRespDto> result = pingService.ping();
    }
}