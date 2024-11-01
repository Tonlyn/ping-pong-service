package com.tek.pingservice

import com.tek.pingservice.constant.ConfigValue
import com.tek.pingservice.constant.Constants
import com.tek.pingservice.dto.PingRespDto
import com.tek.pingservice.service.impl.PingServiceImpl
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Title

@Title("PingServiceImpl Test")
class PingServiceImplTest extends Specification {


    def configValue
    def pingService
    def restTemplate = Mock(RestTemplate)

    //WebClient webClient



    def setup() {
        configValue = new ConfigValue()
        configValue.pongServiceUrl = "http://localhost:8080/pong"
        configValue.fileLockDir = "locks"
        configValue.fileLockFileName = "fileLock"
        configValue.fileLockNum = 2

        pingService = new PingServiceImpl(configValue, restTemplate);

        /*
        def webClientBuilder = Mock(WebClient.Builder)
        webClient = Mock(WebClient)
        webClientBuilder.baseUrl("http://localhost:8080") >> webClientBuilder
        WebClient.Builder.build() >> webClient
        */
    }

    def "test PingService.ping return 200"() {
        given:
        PingRespDto pingRespDto = new PingRespDto(Constants.DTO_RES_SUCCESS, "World");
        def response = new ResponseEntity("World", HttpStatus.OK)

        when:
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(Constants.PING_SEND_CONTENT, headers);
        restTemplate.exchange(configValue.pongServiceUrl, HttpMethod.POST, entity, String.class) >> response

        then:
        def result = pingService.ping().block().message
        pingRespDto.getMessage().equals(result)
    }


    def "test PingService.ping return 429"() {
        given:
        PingRespDto pingRespDto = new PingRespDto(Constants.DTO_RES_FAIL);
        def response = new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS)

        when:
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(Constants.PING_SEND_CONTENT, headers);
        restTemplate.exchange(configValue.pongServiceUrl, HttpMethod.POST, entity, String.class) >> response

        then:
        def result = pingService.ping().block().code
        pingRespDto.getCode().equals(result)
    }


    def "test PingService.ping return other httpStatus,eg: 500"() {
        given:
        PingRespDto pingRespDto = new PingRespDto(Constants.DTO_RES_FAIL);
        def response = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        when:
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(Constants.PING_SEND_CONTENT, headers);
        restTemplate.exchange(configValue.pongServiceUrl, HttpMethod.POST, entity, String.class) >> response

        then:
        def result = pingService.ping().block().code
        pingRespDto.getCode().equals(result)
    }

    def "test get file lock fail"() {
        given:
        configValue.fileLockNum = 0

        def response = new ResponseEntity("World", HttpStatus.OK)

        when:
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(Constants.PING_SEND_CONTENT, headers);
        restTemplate.exchange(configValue.pongServiceUrl, HttpMethod.POST, entity, String.class) >> response

        then:
        def result = pingService.ping().block().code
        result.equals(Constants.DTO_RES_FAIL)
    }


    def "test PingService throws exception"() {
        given:
        configValue = null
        PingRespDto pingRespDto = new PingRespDto(Constants.DTO_RES_FAIL);

        when:
        def result = pingService.ping().block().code

        then:
        result.equals(Constants.DTO_RES_FAIL)
    }


    /*
    def "test 1 ping in 1 second"() {
        given:
        def mockUriSpec = Mock(WebClient.RequestBodyUriSpec)
        def mockSpec = Mock(WebClient.RequestBodySpec)
        def mockHeaderSpec = Mock(WebClient.RequestHeadersSpec)

        webClient.post() >> mockUriSpec
        mockUriSpec.uri(configValue.pongServiceUrl) >> mockSpec
        mockSpec.bodyValue("Hello") >> mockHeaderSpec


        PingRespDto pingRespDto = new PingRespDto(Constants.DTO_RES_SUCCESS, "World");
        def resp = Mono.just(pingRespDto)

        1 * mockHeaderSpec.exchangeToMono(_) >> resp
        resp.doOnError(_) >> resp

        when:
        def result = pingService.ping().block().getMessage()

        then:
        result == "World"
    }*/


}

