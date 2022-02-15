package com.dappercloud.bankholiday;

import com.dappercloud.bankholiday.unitedstatesholiday.UnitedStatesHolidayReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankHolidayQueueConfiguration {
  @Value("${TOPIC_EXCHANGE_NAME:bank-holiday}")
  private String topicExchangeName;

  @Value("${QUEUE_NAME:add-united-states-holiday}")
  private String queueName;

  @Value("${ROUTING_KEY:add.united.states}")
  private String routingKey;

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(UnitedStatesHolidayReceiver unitedStatesHolidayReceiver) {
    return new MessageListenerAdapter(unitedStatesHolidayReceiver, "receiveMessage");
  }

}
