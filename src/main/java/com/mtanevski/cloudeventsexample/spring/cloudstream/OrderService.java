package com.mtanevski.cloudeventsexample.spring.cloudstream;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@Slf4j
public class OrderService {

  private static final Random r = new Random();
  private static long orderId = 0;
  LinkedList<Order> buyOrders = new LinkedList<>(List.of(
      new Order(++orderId, 1, LocalDateTime.now(), OrderType.BUY, 1000),
      new Order(++orderId, 2, LocalDateTime.now(), OrderType.BUY, 1050),
      new Order(++orderId, 3, LocalDateTime.now(), OrderType.BUY, 1030),
      new Order(++orderId, 4, LocalDateTime.now(), OrderType.BUY, 1050),
      new Order(++orderId, 5, LocalDateTime.now(), OrderType.BUY, 1000),
      new Order(++orderId, 11, LocalDateTime.now(), OrderType.BUY, 1050)
  ));

  LinkedList<Order> sellOrders = new LinkedList<>(List.of(
      new Order(++orderId, 6, LocalDateTime.now(), OrderType.SELL, 950),
      new Order(++orderId, 7, LocalDateTime.now(), OrderType.SELL, 1000),
      new Order(++orderId, 8, LocalDateTime.now(), OrderType.SELL, 1050),
      new Order(++orderId, 9, LocalDateTime.now(), OrderType.SELL, 1000),
      new Order(++orderId, 10, LocalDateTime.now(), OrderType.SELL, 1020)
  ));


  @Bean
  public Supplier<Message<Order>> orderBuySupplier() {
    return () -> {
      if (buyOrders.peek() != null) {
        Message<Order> o = MessageBuilder
            .withPayload(buyOrders.peek())
            .setHeader("message", Objects.requireNonNull(buyOrders.poll()).getId())
            .build();
        log.info("Order: {}", o.getPayload());
        return o;
      } else {
        return null;
      }
    };
  }

  @Bean
  public Supplier<Message<Order>> orderSellSupplier() {
    return () -> {
      if (sellOrders.peek() != null) {
        Message<Order> o = MessageBuilder
            .withPayload(sellOrders.peek())
            .setHeader("message", Objects.requireNonNull(sellOrders.poll()).getId())
            .build();
        log.info("Order: {}", o.getPayload());
        return o;
      } else {
        return null;
      }
    };
  }

}
