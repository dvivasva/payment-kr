package com.dvivasva.payment.message;

import com.dvivasva.payment.entity.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class Sender {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
    @Autowired
    private KafkaTemplate<String, Payment> kafkaTemplate;

    public void send(Payment data){
        LOG.info("sending data='{}' to topic='{}'", data, "payment-request");
        kafkaTemplate.send("payment-request", data);
    }
}
