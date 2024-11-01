package com.tek.pingservice


import com.tek.pingservice.controller.PingController
import com.tek.pingservice.dto.PingRespDto
import com.tek.pingservice.service.IPingService
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Title

@Title("PingController Test")
class PingControllerTest extends Specification {

    private def pingService = Mock(IPingService)

    private def pingController = new PingController(pingService);


    def "test PingController.ping"() {
        PingRespDto pingRespDto = new PingRespDto()
        pingRespDto.setCode("success")
        pingRespDto.setMessage("World")
        def res = Mono.just(pingRespDto)

        given:
        pingService.ping() >> res

        when:
        def result = pingController.ping()

        then:
        result.block().getMessage() == "World"
    }



    /*
    def "test PingController"() {

        given:
        String url = "/ping";

        when:
        def resp = mockMvc.perform(get(url)).andReturn().response

        then:
        resp.status == HttpStatus.OK.value()
    }*/
}
