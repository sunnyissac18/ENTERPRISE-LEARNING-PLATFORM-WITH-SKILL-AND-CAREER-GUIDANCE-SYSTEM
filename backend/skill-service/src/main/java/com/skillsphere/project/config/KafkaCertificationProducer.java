package com.skillsphere.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaCertificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRenewalEvent(String message) {
        kafkaTemplate
                .send(
                "certification-renewal",
                message);
    }
}
