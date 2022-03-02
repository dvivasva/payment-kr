package com.dvivasva.payment.listener;

import com.dvivasva.payment.component.PaymentComponent;
import com.dvivasva.payment.entity.Account;
import com.dvivasva.payment.entity.Payment;
import com.dvivasva.payment.service.KafkaProducer;
import com.dvivasva.payment.utils.JsonUtils;
import com.dvivasva.payment.utils.Topic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class KafkaConsumer {


    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final PaymentComponent  paymentComponent;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = Topic.INS_PAYMENT, groupId = "group_id")
    public void consumeGateway(String param) {
        logger.info("Has been published an insert payment from service gateway-mobile : " + param);

        var result=Mono.just(getPayment(param));
        result.doOnNext(payment -> {
              kafkaProducer.sendCellOriginToWallet(payment.getNumberPhoneOrigin());
              kafkaProducer.sendCellDestinationToWallet(payment.getNumberPhoneDestination());

              logger.info("send  message to wallet -->");
              // createPayment(payment);
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

    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_ORIGIN, groupId = "group_id")
    public void consumeResponseAccountOrigin(String param) {
        logger.info("Has been published an response account origin from service account-kr : " + param);
       /* var result=Mono.just(getPayment(param));
        result.doOnNext(payment -> {
            kafkaProducer.sendCellOriginToWallet(payment.getNumberPhoneOrigin());
            kafkaProducer.sendCellDestinationToWallet(payment.getNumberPhoneDestination());

            logger.info("send  message to wallet -->");
            // createPayment(payment);
        }).subscribe();*/

    }
    @KafkaListener(topics = Topic.RESPONSE_ACCOUNT_ORIGIN, groupId = "group_id")
    public void consumeResponseAccountDestination(String param) {
        logger.info("Has been published an response account origin from service account-kr : " + param);

    }















    Account getAccount(String paramX) {
        Account account = null;
        try {
            account = JsonUtils.convertFromJsonToObject(paramX, Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return account;
    }







    public void createPayment(String param) {

        var payment = new Payment();
        try {
            payment = JsonUtils.convertFromJsonToObject(param, Payment.class);

            var result = Mono.just(payment)
                    .map(p -> {

                        var x = getAccount(p.getNumberPhoneOrigin());
                        var y = getAccount(p.getNumberPhoneDestination());

                        // two events

                        var account1 = Mono.just(x)
                                .map(account -> {
                                    account.setAvailableBalance(account.getAvailableBalance() - p.getAmount());
                                    // sendToMSAccount(account)
                                    return account;
                                });

                        var account2 = Mono.just(y)
                                .map(account -> {
                                    account.setAvailableBalance(account.getAvailableBalance() + p.getAmount());
                                    // sendToMSAccount(account)
                                    return account;
                                });

                        return paymentComponent.create(p);

                    });

            result.doOnNext(p -> logger.info("registry success" + p))
                    .subscribe();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*

    @KafkaListener(topics = "ins-payment-json", groupId = "group_json",
            containerFactory = "paymentKafkaListenerFactory")
    public void consumeJson(Payment payment) {
        logger.info("Consumed JSON Message: " + payment);
    }*/


}
