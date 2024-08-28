package com.wallet.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.user.dto.UserDto;
import com.wallet.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CreateUserListener {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    @KafkaListener(topics = "${kafka.topic.user-topic-create}", groupId = "user-service-group")
    public void listenUserCreation(ConsumerRecord<String, String> record) {
        try {
            log.info("Received message: " + record.value());
            UserDto userDto = objectMapper.readValue(record.value(), UserDto.class);
            userService.create(userDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Failed to deserialize message: " + record.value());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to create user from message: " + record.value());
        }
    }
}
