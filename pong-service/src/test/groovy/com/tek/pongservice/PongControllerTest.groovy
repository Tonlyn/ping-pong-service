package com.tek.pongservice

import com.tek.pongservice.constant.Constants
import com.tek.pongservice.controller.PongController
import com.tek.pongservice.dto.PongRespDto
import com.tek.pongservice.service.IPongService
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Title

@Title("PongController Test")
class PongControllerTest extends Specification{

    def pongService = Mock(IPongService)

    def pongController = new PongController(pongService)


    def "test PongController.pong"() {
        given:
        def message = "Hello"
        def pongRespDto = new PongRespDto();
        pongRespDto.setCode("" + HttpStatus.OK.value());
        pongRespDto.setMessage(Constants.PONG_RESP_CONTENT);
        def resp = Mono.just(pongRespDto)

        when:
        pongService.pong(message) >> resp

        then:
        def result = pongController.pong(message).block().getMessage()
        "World".equals(result)
    }
}
