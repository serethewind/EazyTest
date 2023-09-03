package com.eazytest.Notificationservice.communication;

import com.eazytest.Notificationservice.dto.EmailDetails;
import com.eazytest.Notificationservice.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.view.quiz.session.email.queue.name}")
    public void sendViewQuizSessionEmailNotification(EmailDetails emailDetails) {
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
    @RabbitListener(queues = "${rabbitmq.view.quiz.session.score.email.queue.name}")
    public void sendViewQuizSessionScoreEmailNotification(EmailDetails emailDetails) {
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
    @RabbitListener(queues = "${rabbitmq.create.quiz.session.email.queue.name}")
    public void sendCreateQuizSessionEmailNotification(EmailDetails emailDetails) {
        emailService.sendSimpleMessage(emailDetails);
        log.info(String.format("Message sent -> %s", emailDetails));
    }
}
