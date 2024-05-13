package edu.stanford.webprotege.github;

import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import edu.stanford.protege.webprotege.ipc.impl.RabbitMqProperties;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WebProtegeCommonConfiguration.class,
        WebProtegeIpcApplication.class,
        WebProtegeJacksonApplication.class})
@EnableConfigurationProperties
public class WebprotegeGhIntegrationServiceApplication {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeGhIntegrationServiceApplication.class, args);
    }
}
