package com.dvivasva.payment.service;

import com.dvivasva.payment.utils.Topic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducer {

   private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final  KafkaTemplate<String, String> kafkaTemplate;

    public void sendCellOriginToWallet(String value) {
        kafkaTemplate.send(Topic.FIND_CELL_ORIGIN,value);
        logger.info("Messages successfully pushed on topic: " + Topic.FIND_CELL_ORIGIN);
    }
    public void sendCellDestinationToWallet(String value) {
        kafkaTemplate.send(Topic.FIND_CELL_ORIGIN,value);
        logger.info("Messages successfully pushed on topic: " + Topic.FIND_CELL_DESTINATION);
    }
}
