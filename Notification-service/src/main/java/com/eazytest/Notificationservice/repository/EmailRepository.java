package com.eazytest.Notificationservice.repository;

import com.eazytest.Notificationservice.model.EmailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailEntity, String> {
}
