package com.limited.sales.utils;

import com.limited.sales.product.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {

  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "limited.orders")
  public void send(final Product product) {
    MessageConverter converter = rabbitTemplate.getMessageConverter();
    MessageProperties properties = new MessageProperties();
    Message message = converter.toMessage(product, properties);
    rabbitTemplate.send("limited.orders", message);
    log.info("consumer consumes message: {}", message);
  }
}
