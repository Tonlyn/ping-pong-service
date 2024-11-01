package com.tek.pongservice


import com.tek.pongservice.service.impl.PongServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Title

@Title("PongServiceApplication Test")
@SpringBootTest(classes = PongServiceApplication.class, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class PongServiceApplicationTest extends Specification {

    @Autowired
    PongServiceImpl pongService

    def "test PongServiceApplication start"() {
        expect:
        pongService != null
    }
}
