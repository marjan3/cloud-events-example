package com.mtanevski.cloudeventsexample.spring.http;

import static io.cloudevents.core.CloudEventUtils.mapData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtanevski.cloudeventsexample.core.SampleCloudEvent;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.PojoCloudEventDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebMvcController {

  @Autowired
  ObjectMapper objectMapper;

  @PostMapping("webmvc-fire")
  public ResponseEntity<CloudEvent> fire(@RequestBody CloudEvent inputEvent) {

    var cloudEventData = mapData(inputEvent, PojoCloudEventDataMapper.from(objectMapper, SampleCloudEvent.class));

    var sampleData = cloudEventData.getValue();

    var outputEvent = CloudEventBuilder.from(inputEvent)
        .withData(PojoCloudEventData.wrap(sampleData, objectMapper::writeValueAsBytes))
        .build();

    return ResponseEntity.ok(outputEvent);
  }

}
