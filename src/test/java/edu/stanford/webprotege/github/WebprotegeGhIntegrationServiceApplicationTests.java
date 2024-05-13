package edu.stanford.webprotege.github;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@ExtendWith({RabbitMqTestExtension.class, MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class WebprotegeGhIntegrationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
