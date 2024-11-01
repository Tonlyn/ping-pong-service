package com.tek.pingservice.service.impl;

import com.tek.pingservice.constant.ConfigValue;
import com.tek.pingservice.constant.Constants;
import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.lock.AbstractLockHandler;
import com.tek.pingservice.service.IPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class PingServiceImpl extends AbstractLockHandler implements IPingService {

    private static final Logger logger = LoggerFactory.getLogger(PingServiceImpl.class);

    @Autowired
    private ConfigValue configValue;

    @Autowired
    private RestTemplate restTemplate;

    public PingServiceImpl(ConfigValue configValue, RestTemplate restTemplate) {
        this.configValue = configValue;
        this.restTemplate = restTemplate;
    }

    @Override
    public Mono<PingRespDto> ping() {
        try {
            return handle();
        } catch (Exception e) {
            logger.error("ping say Hello fail：" + e.getMessage());
            return Mono.just(new PingRespDto(Constants.DTO_RES_FAIL, e.getMessage()));
        }

    }

    /*
    private WebClient webClient() {
        return WebClient.builder().build();
    }*/

    @Override
    public Mono<PingRespDto> doBusiness() {
        return invokeByRestTemplate();
    }


    private Mono<PingRespDto> invokeByRestTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(Constants.PING_SEND_CONTENT, headers);

        ResponseEntity<String> response = restTemplate.exchange(configValue.PONG_SERVICE_URL, HttpMethod.POST, entity, String.class);
        HttpStatusCode statusCode = response.getStatusCode();

        String msg = "";
        if (HttpStatus.OK.equals(statusCode)) {
            logger.info("Request sent & Pong Respond.");
            String result = response.getBody();
            return Mono.just(new PingRespDto(Constants.DTO_RES_SUCCESS, result));
        } else if (HttpStatus.TOO_MANY_REQUESTS == statusCode) {
            msg = "Request sent & Pong throttled it.";
            logger.warn(msg);
            return Mono.just(new PingRespDto(Constants.DTO_RES_FAIL, msg));
        } else {
            msg = "Received unexpected response: " + statusCode;
            logger.warn(msg);
            return Mono.just(new PingRespDto(Constants.DTO_RES_FAIL, msg));
        }
    }

    /*
    private Mono<PingRespDto> invokeByWebClient() {
        return webClient().post()
                .uri(configValue.pongServiceUrl)
                .bodyValue(Constants.PING_SEND_CONTENT)
                .exchangeToMono(this::handleResponse)
                .doOnError(Exception.class, e -> {
                    Mono.error(new Exception("Invoke Pong API Fail: " + e.getMessage()));
                });
    }


    private Mono<PingRespDto> handleResponse(ClientResponse response) {
        return response
                .bodyToMono(String.class)
                .flatMap(result -> {

                    PingRespDto pingRespDto = new PingRespDto();
                    HttpStatusCode httpStatusCode = response.statusCode();
                    String msg = "";

                    if (httpStatusCode.is2xxSuccessful()) {
                        logger.info("Request sent & Pong Respond.");
                        pingRespDto.setCode(Constants.DTO_RES_SUCCESS);
                        pingRespDto.setMessage(result);
                    } else if (HttpStatus.TOO_MANY_REQUESTS.equals(httpStatusCode)) {
                        msg = "Request sent & Pong throttled it.";
                        logger.warn(msg);
                        pingRespDto.setCode(Constants.DTO_RES_FAIL);
                        pingRespDto.setMessage(msg);
                    } else {
                        msg = "Received unexpected response: " + httpStatusCode;
                        logger.warn(msg);
                        pingRespDto.setCode(Constants.DTO_RES_FAIL);
                        pingRespDto.setMessage(msg);
                    }
                    return Mono.just(pingRespDto);
                });
    }*/

    @Override
    public String lockFail() {
        String msg = "Request not send as being \"rate limited\".";
        logger.warn(msg);
        return msg;
    }

    @Override
    public ConfigValue getConfigValue() {
        return configValue;
    }

}
