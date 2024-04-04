package com.mtanevski.cloudeventsexample.spring.cloudstream;

import io.cloudevents.spring.messaging.CloudEventMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CloudEventMessageConverterConfiguration {

  @Bean
  public CloudEventMessageConverter cloudEventMessageConverter() {
    return new CloudEventMessageConverter();
  }


}
