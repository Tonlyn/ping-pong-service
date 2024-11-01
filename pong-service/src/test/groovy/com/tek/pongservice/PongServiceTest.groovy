package com.tek.pongservice

import com.tek.pongservice.constant.Constants
import com.tek.pongservice.service.impl.PongServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Title

import java.util.concurrent.ConcurrentLinkedDeque

@Title("PongServiceImpl Test")
class PongServiceTest extends Specification {


    def pongService = new PongServiceImpl();

    def setup() {

    }


    def "test PongService.pong return 200"() {
        given:
        def response = ResponseEntity.ok(Constants.PONG_RESP_CONTENT);

        when:
        def result = pongService.pong()

        then:
        result.statusCode == response.statusCode
        result.body == response.body
    }



    def "test PongService.pong return 429"() {
        given:
        //def bucket = Mock(ConcurrentLinkedDeque<Integer>)
        def response = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)


        when:
        def result1 = pongService.pong()
        def result2 = pongService.pong()

        then:
        result1.statusCode == HttpStatus.OK
        result2.statusCode == HttpStatus.TOO_MANY_REQUESTS
    }


}
