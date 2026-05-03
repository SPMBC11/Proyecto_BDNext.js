package edu.puj.publisher.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import edu.puj.publisher.dto.MensajePublicadoDto;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class RabbitMqPublisher {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private ObjectMapper mapper = new ObjectMapper();
    private String queueName;

    private String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    @PostConstruct
    public void init() throws Exception {
        factory = new ConnectionFactory();
        factory.setHost(env("RABBITMQ_HOST", "rabbitmq"));
        factory.setPort(Integer.parseInt(env("RABBITMQ_PORT", "5672")));
        factory.setUsername(env("RABBITMQ_USERNAME", "guest"));
        factory.setPassword(env("RABBITMQ_PASSWORD", "guest"));
        connection = factory.newConnection();
        channel = connection.createChannel();
        queueName = env("RABBITMQ_QUEUE", "mensajes_correo");
        channel.queueDeclare(queueName, true, false, false, null);
    }

    public void publish(MensajePublicadoDto dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes(StandardCharsets.UTF_8));
    }

    @PreDestroy
    public void close() throws Exception {
        if (channel != null) channel.close();
        if (connection != null) connection.close();
    }
}
