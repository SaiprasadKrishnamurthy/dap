package org.sai.dap.meta;

import akka.actor.ActorSystem;
import com.google.common.base.Predicates;
import com.mongodb.MongoClient;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.sai.dap.meta.config.ActorFactory;
import org.sai.dap.meta.config.AppProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;

/**
 * Created by saipkri on 07/09/16.
 */
@ComponentScan("org.sai.dap.meta")
@EnableAutoConfiguration
@EnableAsync
@PropertySource("classpath:application.properties")
@EnableSwagger2
@Configuration
public class DapApplication {

    @Inject
    private AppProperties appProperties;

    private ActorSystem actorSystem() {
        return ActorSystem.create("DapActorSystem");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ActorFactory actorFactory() throws Exception {
        return new ActorFactory(actorSystem(), appProperties, mongoTemplate(), jmsTemplate());
    }

    private ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(appProperties.getAmqUrl());
        return connectionFactory;
    }

    private JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }

    private SimpleMongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(appProperties.getMongoHost()), "dap");
    }

    private MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    /**
     * Swagger 2 docket bean configuration.
     *
     * @return swagger 2 Docket.
     */
    @Bean
    public Docket resolverApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("dap")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error"))) // Exclude Spring error controllers
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Data Acquisition Processor (DAP) REST API")
                .contact("sai@concordesearch.co.uk")
                .version("1.0")
                .build();
    }

    public static void main(String[] args) {
        SpringApplicationBuilder application = new SpringApplicationBuilder();
        application //
                .headless(true) //
                .addCommandLineProperties(true) //
                .sources(DapApplication.class) //
                .main(DapApplication.class) //
                .registerShutdownHook(true)
                .run(args);
    }


}
