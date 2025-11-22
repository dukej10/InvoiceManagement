package co.com.management.consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.adapter.restconsumer")
public class RestConsumerConfig {

    private  String baseUrl;

    private  int connectTimeoutMs;

    private  int readTimeoutMs;


}