package com.tek.pingservice.service.impl;

import com.tek.pingservice.constant.ConfigValue;
import com.tek.pingservice.constant.Constants;
import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.lock.AbstractLockHandler;
import com.tek.pingservice.service.IPingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Ping Service Implement class
 *
 * @author linshy
 * @date 2024/10/30
 */
@Service
public class PingServiceImpl extends AbstractLockHandler implements IPingService {

    private static final Logger logger = LoggerFactory.getLogger(PingServiceImpl.class);

    @Autowired
    private ConfigValue configValue;


    private final WebClient webClient;

    public PingServiceImpl(ConfigValue configValue, WebClient.Builder webClientBuilder) {
        this.configValue = configValue;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<PingRespDto> ping() {
        try {
            return handle();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Mono.just(new PingRespDto(Constants.DTO_RES_FAIL, e.getMessage()));
        }
    }


    @Override
    public Mono<PingRespDto> doBusiness() {
        return invokeByWebClient();
    }


    /**
     * to invoke Pong Service using webclient
     * @return
     */
    private Mono<PingRespDto> invokeByWebClient() {
        return webClient.post()
                .uri(configValue.PONG_SERVICE_URL)
                .bodyValue(Constants.PING_SEND_CONTENT)
                .exchangeToMono(this::handleResponse)
                .onErrorReturn(new PingRespDto("", "webclient error"));
    }


    /**
     * response handler for webclient
     * @param response
     * @return
     */
    private Mono<PingRespDto> handleResponse(ClientResponse response) {

        return response
                .bodyToMono(PingRespDto.class)
                .flatMap(dto -> {

                    HttpStatusCode httpStatusCode = response.statusCode();
                    String code = dto.getCode();

                    if (code.equals("" + HttpStatus.OK.value())) {
                        logger.info("Request sent & Pong Respond.");
                        return Mono.just(new PingRespDto(code, dto.getMessage()));

                    } else if (code.equals("" + HttpStatus.TOO_MANY_REQUESTS.value())) {
                        String msg = "Request sent & Pong throttled it.";
                        logger.warn(msg);
                        return Mono.just(new PingRespDto(code, msg));

                    } else {
                        String msg = "Received unexpected response: " + httpStatusCode.value();
                        logger.warn(msg);
                        return Mono.just(new PingRespDto("" + httpStatusCode.value(), msg));
                    }
                });
    }

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
