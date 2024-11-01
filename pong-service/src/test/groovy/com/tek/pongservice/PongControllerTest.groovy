package com.tek.pongservice

import com.tek.pongservice.controller.PongController
import com.tek.pongservice.service.IPongService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Title

@Title("PongController Test")
class PongControllerTest extends Specification{

    def pongService = Mock(IPongService)

    def pongController = new PongController(pongService)


    def "test PongController.pong"() {
        given:
        def message = "Hello"
        def response = new ResponseEntity("World", HttpStatus.OK)

        when:
        pongService.pong(message) >> response

        then:
        def result = pongController.pong(message).body
        "World".equals(result)
    }
}
