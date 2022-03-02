package com.dvivasva.payment.service;

import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.repository.IPaymentRepository;
import com.dvivasva.payment.utils.PaymentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final IPaymentRepository paymentRepository;

    public Mono<PaymentDto> create(final Mono<PaymentDto> entityToDto) {
        return entityToDto.map(PaymentUtil::dtoToEntity)
                .flatMap(paymentRepository::save)
                .map(PaymentUtil::entityToDto);

    }

    public Flux<PaymentDto> read() {
        return paymentRepository.findAll().map(PaymentUtil::entityToDto);
    }


}
