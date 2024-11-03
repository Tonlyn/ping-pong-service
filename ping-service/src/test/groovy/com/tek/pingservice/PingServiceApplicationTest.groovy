package com.tek.pingservice

import com.tek.pingservice.task.PingTaskExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Title

@Title("PingServiceApplication Test")
@SpringBootTest(classes = PingServiceApplication.class, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class PingServiceApplicationTest extends Specification {

    @Autowired
    PingTaskExecutor pingTaskExecutor;

    def "test initial PingTaskExecutor"() {
        expect:
        pingTaskExecutor != null
    }



}