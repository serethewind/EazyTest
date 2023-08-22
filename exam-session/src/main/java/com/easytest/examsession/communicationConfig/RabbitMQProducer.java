package com.easytest.examsession.communicationConfig;

import com.easytest.examsession.dto.communication.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.eazytest.exchange.name}")
    private String notificationExchange;

    @Value("${rabbitmq.create.quiz.session.email.routing.key}")
    private String createQuizSessionEmailRoutingKey;

    @Value("${rabbitmq.view.quiz.session.email.routing.key}")
    private String viewQuizSessionRoutingKey;

    @Value("${rabbitmq.view.quiz.session.score.email.routing.key}")
    private String viewQuizSessionScoreRoutingKey;

    public void sendViewQuizSessionEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, viewQuizSessionRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendViewQuizSessionScoreEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, viewQuizSessionScoreRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }

    public void sendCreateQuizSessionEmailNotification(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(notificationExchange, createQuizSessionEmailRoutingKey, emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }


}
