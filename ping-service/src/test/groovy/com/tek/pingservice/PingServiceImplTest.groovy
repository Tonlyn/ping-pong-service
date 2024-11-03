package com.tek.pingservice

import com.tek.pingservice.constant.ConfigValue
import com.tek.pingservice.constant.Constants
import com.tek.pingservice.dto.PingRespDto
import com.tek.pingservice.service.impl.PingServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Title

@Title("PingServiceImpl Test")
class PingServiceImplTest extends Specification {



    def configValue

    def webClient = Mock(WebClient)
    def webClientBuilder = Mock(WebClient.Builder)
    def mockUriSpec = Mock(WebClient.RequestBodyUriSpec)
    def mockSpec = Mock(WebClient.RequestBodySpec)
    def mockHeaderSpec = Mock(WebClient.RequestHeadersSpec)

    def pingService


    def setup() {
        configValue = new ConfigValue()
        configValue.PONG_SERVICE_URL = "http://localhost:8080/pong"
        configValue.FILE_LOCK_DIR = "locks"
        configValue.FILE_LOCK_FILE_NAME = "fileLock"
        configValue.FILE_LOCK_NUM = 2

        webClientBuilder.build() >> webClient
        webClient.post() >> mockUriSpec
        mockUriSpec.uri(configValue.PONG_SERVICE_URL) >> mockSpec
        mockSpec.bodyValue("Hello") >> mockHeaderSpec

        pingService = new PingServiceImpl(configValue, webClientBuilder);
    }





    def "test PingService.ping return 200"() {
        given:
        PingRespDto pingRespDto = new PingRespDto("" + HttpStatus.OK.value(), "World");
        Mono<PingRespDto> resp = Mono.just(pingRespDto);

        mockHeaderSpec.exchangeToMono(_) >> resp
        resp.onErrorReturn(_) >> resp

        when:
        def message = pingService.ping().block().getMessage()

        then:
        message.equals("World")
    }




    def "test PingService.handleResponse with 200"() {
        given:
        PingRespDto pingRespDto = new PingRespDto("" + HttpStatus.OK.value(), "World");
        def resp = Mono.just(pingRespDto)


        def mockResponse = Mock(ClientResponse)
        mockResponse.statusCode() >> HttpStatus.OK
        mockResponse.bodyToMono(PingRespDto.class) >> resp

        mockHeaderSpec.exchangeToMono(_) >> resp
        resp.onErrorReturn(_) >> resp


        when:
        def result = pingService.handleResponse(mockResponse).block()

        then:
        result.getCode().equals("200")
        result.getMessage().equals("World")
    }


    def "test PingService.handleResponse with 429"() {
        given:
        PingRespDto pingRespDto = new PingRespDto("" + HttpStatus.TOO_MANY_REQUESTS.value());
        def resp = Mono.just(pingRespDto)

        def mockResponse = Mock(ClientResponse)
        mockResponse.statusCode() >> HttpStatus.OK
        mockResponse.bodyToMono(PingRespDto.class) >> resp

        mockHeaderSpec.exchangeToMono(_) >> resp
        resp.onErrorReturn(_) >> resp


        when:
        def result = pingService.handleResponse(mockResponse).block()

        then:
        result.getCode().equals("429")
    }


    def "test PingService.handleResponse with unexpected response"() {
        given:
        PingRespDto pingRespDto = new PingRespDto("" + HttpStatus.INTERNAL_SERVER_ERROR.value());
        def resp = Mono.just(pingRespDto)

        def mockResponse = Mock(ClientResponse)
        mockResponse.statusCode() >> HttpStatus.INTERNAL_SERVER_ERROR
        mockResponse.bodyToMono(PingRespDto.class) >> resp

        mockHeaderSpec.exchangeToMono(_) >> resp
        resp.onErrorReturn(_) >> resp


        when:
        def result = pingService.handleResponse(mockResponse).block()

        then:
        result.getCode().equals("500")
    }



    def "test get file lock fail"() {
        given:
        configValue.FILE_LOCK_NUM = 0

        when:
        def code = pingService.ping().block().getCode()

        then:
        code.equals(Constants.DTO_RES_FAIL)
    }


    def "test PingService throws exception"() {
        given:
        configValue = null

        when:
        def code = pingService.ping().block().getCode()

        then:
        code.equals(Constants.DTO_RES_FAIL)
    }


}
