package com.eazytest.Notificationservice.service;

import com.eazytest.Notificationservice.dto.EmailDetails;

public interface EmailService {
    String sendSimpleMessage(EmailDetails emailDetails);

    String sendMessageWithAttachment(EmailDetails emailDetails);
}
