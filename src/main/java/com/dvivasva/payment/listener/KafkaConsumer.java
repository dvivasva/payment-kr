package com.dvivasva.payment.listener;

import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.entity.Account;
import com.dvivasva.payment.entity.Payment;
import com.dvivasva.payment.service.KafkaProducer;
import com.dvivasva.payment.service.PaymentService;
import com.dvivasva.payment.utils.DateUtil;
import com.dvivasva.payment.utils.JsonUtils;
import com.dvivasva.payment.utils.Topic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class KafkaConsumer {


    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final PaymentService paymentService;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = Topic.INS_PAYMENT, groupId = "group_id")
    public void consumeGateway(String param) {
        logger.info("Has been published an insert payment from service gateway-mobile : " + param);

        var result = Mono.just(getPayment(param));
        result.doOnNext(payment -> {
            kafkaProducer.sendCellOriginToWallet(payment.getNumberPhoneOrigin());
            kafkaProducer.sendCellDestinationToWallet(payment.getNumberPhoneDestination());

            logger.info("send  message to wallet -->");
            createPayment(param);
        }).subscribe();

    }

    Payment getPayment(String param) {
        Payment payment = null;
        try {
            payment = JsonUtils.convertFromJsonToObject(param, Payment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payment;
    }

    Account accountOrigin=null;
    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_ORIGIN, groupId = "group_id")
    public void consumeResponseAccountOrigin(String param) {
        logger.info("Has been published an response account origin from service account-kr : " + param);
        Account account = null;
        try {
            account = JsonUtils.convertFromJsonToObject(param, Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountOrigin=account;

    }

    Account accountDestination=null;
    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_DESTINATION, groupId = "group_id")
    public void consumeResponseAccountDestination(String param) {
        logger.info("Has been published an response account destination from service account-kr : " + param);
        Account account = null;
        try {
            account = JsonUtils.convertFromJsonToObject(param, Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountDestination=account;

    }

    public void createPayment(String param) {

        var paymentDto = new PaymentDto();
        try {
            paymentDto = JsonUtils.convertFromJsonToObject(param, PaymentDto.class);
            var result = Mono.just(paymentDto)
                    .map(p -> {

                        // two events
                        accountOrigin.setAvailableBalance(accountOrigin.getAvailableBalance() - p.getAmount());
                        kafkaProducer.requestUpdateAccountOrigin(accountOrigin);

                        accountDestination.setAvailableBalance(accountDestination.getAvailableBalance() + p.getAmount());
                        kafkaProducer.requestUpdateAccountDestination(accountDestination);

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
