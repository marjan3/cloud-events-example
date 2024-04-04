package com.mtanevski.cloudeventsexample.spring.webflux;

import io.cloudevents.CloudEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebFluxController {


  @PostMapping("/webflux-fire")
  public Mono<CloudEvent> event(@RequestBody Mono<CloudEvent> body) {
    return body;
  }

}
