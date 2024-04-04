package com.mtanevski.cloudeventsexample;

import static com.mtanevski.cloudeventsexample.TestData.CLOUD_EVENT_HEADERS;
import static com.mtanevski.cloudeventsexample.TestData.SAMPLE_CLOUD_EVENT;
import static org.assertj.core.api.Assertions.assertThat;

import com.mtanevski.cloudeventsexample.httpbasic.BasicHttpCloudEventServer;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

class BasicHttpCloudEventTest {

  private final BasicHttpCloudEventServer server = new BasicHttpCloudEventServer(0);

  @Test
  void shouldFireCloudEventBasicHttpExample() {
    // given
    server.start();

    // when
    var response = Unirest.post(server.getAddress() + "/fire")
        .headers(CLOUD_EVENT_HEADERS)
        .body(SAMPLE_CLOUD_EVENT)
        .asString();

    // then
    assertThat(response.getBody()).isEqualTo(SAMPLE_CLOUD_EVENT);
    assertThat(String.join(",", response.getHeaders().get("Ce-specversion")))
        .isEqualTo(CLOUD_EVENT_HEADERS.get("ce-specversion"));
    assertThat(String.join(",", response.getHeaders().get("Ce-type")))
        .isEqualTo(CLOUD_EVENT_HEADERS.get("ce-type"));
    assertThat(String.join(",", response.getHeaders().get("Ce-source")))
        .isEqualTo(CLOUD_EVENT_HEADERS.get("ce-source"));
    assertThat(String.join(",", response.getHeaders().get("Ce-id")))
        .isEqualTo(CLOUD_EVENT_HEADERS.get("ce-id"));
    server.stop();
  }

}
