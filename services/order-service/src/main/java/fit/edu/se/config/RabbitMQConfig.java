package fit.edu.se.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String ORDER_QUEUE = "order.queue";
	public static final String ORDER_EXCHANGE = "order.exchange";
	public static final String ROUTING_KEY = "order.routing";

	@Bean
	Queue orderQueue() {
		return new Queue(ORDER_QUEUE, true);
	}

	@Bean
	TopicExchange orderExchange() {
		return new TopicExchange(ORDER_EXCHANGE);
	}

	@Bean
	Binding binding(Queue orderQueue, TopicExchange orderExchange) {
		return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY);
	}
}
