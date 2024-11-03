package com.tek.pongservice

import com.tek.pongservice.constant.Constants
import com.tek.pongservice.dto.PongRespDto
import com.tek.pongservice.service.impl.PongServiceImpl
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Title

@Title("PongServiceImpl Test")
class PongServiceTest extends Specification {


    def pongService = new PongServiceImpl();

    def setup() {

    }


    def "test PongService.pong return 200"() {
        given:
        def message = "Hello"
        def resp = new PongRespDto("" + HttpStatus.OK.value(), Constants.PONG_RESP_CONTENT)

        when:
        def result = pongService.pong(message).block()

        then:
        result.getCode() == resp.getCode()
        result.getMessage() == resp.getMessage()
    }



    def "test PongService.pong return 429"() {
        given:
        def message = "Hello"
        def resp = new PongRespDto("" + HttpStatus.TOO_MANY_REQUESTS.value())

        when:
        def result1 = pongService.pong(message).block()
        def result2 = pongService.pong(message).block()

        then:
        result1.getCode() == "200"
        result2.getCode() == "429"
    }


}
