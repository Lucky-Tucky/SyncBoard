package com.backend.SyncBoard.Config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {

    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaConfig(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate= kafkaTemplate;
    }

    public void kafkaProducer(String topic , String body){
        kafkaTemplate.send(topic,body).whenComplete((result, ex) -> {

            if (ex != null) {
                System.out.println("KAFKA ERROR: " + ex.getMessage());
            } else {
                System.out.println(
                        "KAFKA SUCCESS: topic=" +
                                result.getRecordMetadata().topic() +
                                ", partition=" +
                                result.getRecordMetadata().partition() +
                                ", offset=" +
                                result.getRecordMetadata().offset()
                );
            }
        });
    }
}
