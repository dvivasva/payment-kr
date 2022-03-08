package com.dvivasva.payment.message;

import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.service.PaymentService;
import com.dvivasva.payment.utils.DateUtil;
import com.dvivasva.payment.utils.JsonUtils;
import com.dvivasva.payment.utils.Topic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class Receive {

    private static final Logger logger = LoggerFactory.getLogger(Receive.class);
    private final PaymentService paymentService;


    @KafkaListener(topics = Topic.RESPONSE_PAYMENT_ON_PAYMENT, groupId = "group_id_payment")
    public void consumeWallet(String param) {
        logger.info("Has been published an insert payment from service wallet-kr : " + param);

        createPayment(param);
    }

    public void createPayment(String param) {

        var paymentDto = new PaymentDto();
        try {
            paymentDto = JsonUtils.convertFromJsonToObject(param, PaymentDto.class);

            var result = Mono.just(paymentDto)
                    .map(p -> {

                        var today = LocalDateTime.now();
                        p.setDate(DateUtil.toDate(today));
                        return p;
                    });

            paymentService.create(result).doOnNext(p -> logger.info("registry success" + p))
                    .subscribe();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
