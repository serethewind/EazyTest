package com.easytest.examsession.communicationConfig;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.eazytest.exchange.name}")
    private String notificationExchange;

    @Value("${rabbitmq.create.quiz.session.email.queue.name}")
    private String createQuizSessionEmailQueue;

    @Value("${rabbitmq.view.quiz.session.email.queue.name}")
    private String viewQuizSessionEmailQueue;

    @Value("${rabbitmq.view.quiz.session.score.email.queue.name}")
    private String viewQuizSessionScoreEmailQueue;

    @Value("${rabbitmq.create.quiz.session.email.routing.key}")
    private String createQuizSessionEmailRoutingKey;

    @Value("${rabbitmq.view.quiz.session.email.routing.key}")
    private String viewQuizSessionRoutingKey;

    @Value("${rabbitmq.view.quiz.session.score.email.routing.key}")
    private String viewQuizSessionScoreRoutingKey;


    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchange);
    }

    @Bean
    public Queue viewQuizSessionScoreQueue() {
        return new Queue(viewQuizSessionScoreEmailQueue);
    }

    @Bean
    public Queue createQuizSessionQueue() {
        return new Queue(createQuizSessionEmailQueue);
    }

    @Bean
    public Queue viewQuizSessionQueue() {
        return new Queue(viewQuizSessionEmailQueue);
    }


    @Bean
    public Binding viewQuizSessionScoreBinding() {
        return BindingBuilder.bind(viewQuizSessionScoreQueue())
                .to(notificationExchange())
                .with(viewQuizSessionScoreRoutingKey);
    }

    @Bean
    public Binding createQuizSessionBinding() {
        return BindingBuilder.bind(createQuizSessionQueue())
                .to(notificationExchange())
                .with(createQuizSessionEmailRoutingKey);
    }
    @Bean
    public Binding viewQuizSessionBinding() {
        return BindingBuilder.bind(viewQuizSessionQueue())
                .to(notificationExchange())
                .with(viewQuizSessionRoutingKey);
    }

    //rabbit template for sending json messages
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    //amqpTemplate is an interface that is implemented by rabbit template. it supports json message
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
