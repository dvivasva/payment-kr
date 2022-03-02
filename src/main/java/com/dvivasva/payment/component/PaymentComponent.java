package com.dvivasva.payment.component;

import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.entity.Payment;
import com.dvivasva.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PaymentComponent {
    private final PaymentRepository paymentRepository;

    public Mono<PaymentDto> create(Payment payment) {
        return paymentRepository.create(payment);
    }

    public Flux<PaymentDto> read() {
        return paymentRepository.read();
    }
}
