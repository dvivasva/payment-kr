package com.dvivasva.payment.repository;

import com.dvivasva.payment.entity.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends ReactiveMongoRepository<Payment, String> {
}
