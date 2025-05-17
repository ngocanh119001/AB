package iuh.fit.se.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

	@Bean
	Queue userEventsQueue() {
	    return new Queue("user-events", true); // durable queue
	}
}
