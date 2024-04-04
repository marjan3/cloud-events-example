package com.mtanevski.cloudeventsexample;

import java.util.Map;

public class TestData {

  public static final String SAMPLE_CLOUD_EVENT = """
      {
          "data": "SAMPLE_DATA",
          "datacontenttype": "application/json; charset=utf-8",
          "id": "EVENT_ID",
          "source": "com.mtanevski.cloudeventsexample",
          "specversion": "1.0",
          "type": "SampleCloudEvent",
          "time": "EVENT_GENERATION_TIME",
          "dataschema": "https://mtanevski.com/SampleCloudEvent.json",
          "methodName": "jobservice.jobcompleted",
          "resourceName": "cloud-events-example",
          "serviceName": "com.mtanevski.cloud-events-example",
          "subject": "JOB_NAME_MAYBE"
      }
      """;
  public static final Map<String, String> CLOUD_EVENT_HEADERS = Map.of(
      "ce-specversion", "1.0",
      "ce-id", "t1",
      "ce-source", "localhost",
      "ce-type", "com.mtanevski.cloudevents-example",
      "Content-Type", "application/json");
}
