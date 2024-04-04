package com.mtanevski.cloudeventsexample;

import static com.mtanevski.cloudeventsexample.TestData.CLOUD_EVENT_HEADERS;
import static com.mtanevski.cloudeventsexample.TestData.SAMPLE_CLOUD_EVENT;
import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringWebmvcCloudEventTest {

  @LocalServerPort
  private int port;

  @Test
  void shouldTest() {
    // when
    var response = Unirest.post("http://localhost:" + port + "/webmvc-fire")
        .body(SAMPLE_CLOUD_EVENT)
        .headers(CLOUD_EVENT_HEADERS)
        .asString();

    // then
    assertThat(response.getBody())
        .isEqualTo("""
            {"id":"EVENT_ID","source":"com.mtanevski.cloudeventsexample","type":"SampleCloudEvent"}""");
  }

}
