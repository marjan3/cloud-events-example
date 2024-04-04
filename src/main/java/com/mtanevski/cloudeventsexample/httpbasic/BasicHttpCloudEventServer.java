package com.mtanevski.cloudeventsexample.httpbasic;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.cloudevents.core.message.MessageReader;
import io.cloudevents.core.message.MessageWriter;
import io.cloudevents.http.HttpMessageFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicHttpCloudEventServer {

  private final HttpServer httpServer;

  @SneakyThrows
  public BasicHttpCloudEventServer(int port) {
    httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
    httpServer.createContext("/fire", BasicHttpCloudEventServer::fireHandler);
    httpServer.setExecutor(Executors.newCachedThreadPool());
  }

  public void start() {
    httpServer.start();
  }

  public void stop() {
    httpServer.stop(0);
  }

  public String getAddress() {
    var address = httpServer.getAddress();
    return "http://" + address.getHostName() + ":" + address.getPort();
  }

  @SneakyThrows
  private static void fireHandler(HttpExchange exchange) {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, 0);
      return;
    }
    try {
      var messageReader = createMessageReader(exchange);
      var cloudEvent = messageReader.toEvent();

      log.info("Handling event: " + cloudEvent);

      var messageWriter = createMessageWriter(exchange);
      messageWriter.writeBinary(cloudEvent);
    } catch (Throwable error) {
      try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
        try (PrintWriter pw = new PrintWriter(byteArrayOutputStream)) {
          error.printStackTrace(pw);
        }
        var body = byteArrayOutputStream.toByteArray();
        exchange.sendResponseHeaders(500, body.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
          outputStream.write(body);
        }
      }
    }
  }

  @SneakyThrows
  public static MessageReader createMessageReader(HttpExchange httpExchange) {
    var headers = httpExchange.getRequestHeaders();
    var body = httpExchange.getRequestBody().readAllBytes();
    return HttpMessageFactory.createReaderFromMultimap(headers, body);
  }

  @SneakyThrows
  public static MessageWriter createMessageWriter(HttpExchange httpExchange) {
    return HttpMessageFactory.createWriter(httpExchange.getResponseHeaders()::add,
        body -> {
          try (OutputStream os = httpExchange.getResponseBody()) {
            if (body != null) {
              httpExchange.getResponseHeaders().add("Content-Type", APPLICATION_JSON_VALUE);
              httpExchange.sendResponseHeaders(200, body.length);
              os.write(body);
            } else {
              httpExchange.sendResponseHeaders(204, -1);
            }
          } catch (IOException ioException) {
            log.error("Error occurred while writing to http output", ioException);
          }
        }
    );
  }
}
