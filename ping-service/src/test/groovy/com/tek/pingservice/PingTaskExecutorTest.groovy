package com.tek.pingservice

import com.tek.pingservice.constant.Constants
import com.tek.pingservice.dto.PingRespDto
import com.tek.pingservice.service.IPingService
import com.tek.pingservice.task.PingTaskExecutor
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Title

@Title("PingTaskExecutor Test")
class PingTaskExecutorTest extends Specification {

    def pingService = Mock(IPingService)

    def pingTaskExecutor = new PingTaskExecutor(pingService)


    def "test PingTaskExecutor.execute"() {

        def result = Mono.just(new PingRespDto(Constants.DTO_RES_SUCCESS, "World"))

        given:
        pingService.ping() >> result

        when:
        pingTaskExecutor.execute()

        then:
        noExceptionThrown()
    }



}
