package com.mtanevski.cloudeventsexample;

import static com.mtanevski.cloudeventsexample.TestData.CLOUD_EVENT_HEADERS;
import static com.mtanevski.cloudeventsexample.TestData.SAMPLE_CLOUD_EVENT;
import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

// can't use webflux and webmvc at the same time
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringWebfluxCloudEventTest {

  @LocalServerPort
  private int port;

  // can't use webflux and webmvc at the same time
//  @Test
  void shouldTest() {
    // when
    var response = Unirest.post("http://localhost:" + port + "/webflux-fire")
        .body(SAMPLE_CLOUD_EVENT)
        .headers(CLOUD_EVENT_HEADERS)
        .asString();

    // then
    assertThat(response.getBody())
        .isEqualTo("""
            {"timestamp":"2024-03-22T12:17:33.036+00:00","status":500,"error":"Internal Server Error","path":"/webflux-fire"}""");
  }

}
