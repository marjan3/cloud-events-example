package com.mtanevski.cloudeventsexample.core;

import lombok.Data;

@Data
public class SampleCloudEvent {

  private final String id;
  private final String source;
  private final String type;
}
