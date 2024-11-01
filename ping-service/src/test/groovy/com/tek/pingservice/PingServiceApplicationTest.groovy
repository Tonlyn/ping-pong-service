package com.tek.pingservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Title

@Title("PingServiceApplication Test")
@SpringBootTest(classes = PingServiceApplication.class, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class PingServiceApplicationTest extends Specification {

    @Autowired
    RestTemplate restTemplate;

    def "test initial RestTemplate"() {
        expect:
        restTemplate != null
    }



}