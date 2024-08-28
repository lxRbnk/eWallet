package com.wallet.auth.kafka;

import com.wallet.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DeleteUserListener {

    private final UserService userService;

    @KafkaListener(topics = "${kafka.topic.user-topic-delete}", groupId = "user-service-group")
    public void listenUserCreation(ConsumerRecord<String, String> record) {
        log.info("Received message: " + record.value());
        userService.delete(record.value());
    }
}
