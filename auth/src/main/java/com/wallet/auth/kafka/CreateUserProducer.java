package com.wallet.auth.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CreateUserProducer {

    @Value("${kafka.topic.user-topic-create}")
    private String userTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CreateUserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreationMessage(String message) {
        kafkaTemplate.send(userTopic, message);
    }
}

