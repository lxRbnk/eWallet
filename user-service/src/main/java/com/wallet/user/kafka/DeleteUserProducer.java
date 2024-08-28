package com.wallet.user.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserProducer {

    @Value("${kafka.topic.user-topic-delete}")
    private String userTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DeleteUserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserDeleteMessage(String message) {
        kafkaTemplate.send(userTopic, message);
    }
}

