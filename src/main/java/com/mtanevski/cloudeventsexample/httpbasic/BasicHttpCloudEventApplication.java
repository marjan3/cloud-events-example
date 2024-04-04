package com.mtanevski.cloudeventsexample.httpbasic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicHttpCloudEventApplication {

  public static void main(String[] args) {
    BasicHttpCloudEventServer basicHttpCloudEventServer = new BasicHttpCloudEventServer(0);
    basicHttpCloudEventServer.start();
  }

}
