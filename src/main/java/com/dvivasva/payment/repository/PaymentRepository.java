package com.dvivasva.payment.repository;


import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.entity.Payment;
import com.dvivasva.payment.utils.PaymentUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);
    private static final String KEY = "Payment";
    private final ReactiveRedisOperations<String, Payment> redisOperations;
    private final ReactiveHashOperations<String, String, Payment> hashOperations;


    @Autowired
    public PaymentRepository(ReactiveRedisOperations<String, Payment> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }

    public Mono<PaymentDto> create(Payment payment) {
        logger.info("inside methode create");
        if (payment.getId() != null) {
            String id = UUID.randomUUID().toString();
            payment.setId(id);
        }
        return hashOperations.put(KEY, payment.getId(), payment)
                .map(isSaved -> payment).map(PaymentUtil::entityToDto);
    }

    public Mono<Boolean> existsById(String id) {
        return hashOperations.hasKey(KEY, id);
    }

    public Flux<PaymentDto> read() {
        return hashOperations.values(KEY).map(PaymentUtil::entityToDto);
    }


}
