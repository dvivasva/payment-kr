package com.dvivasva.payment.controller;


import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    /**
     * @param wallet .
     * @return status 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PaymentDto> create(
            @RequestBody final Mono<PaymentDto> wallet) {
        return paymentService.create(wallet);
    }

    /**
     * @return flux .
     */
    @GetMapping
    public Flux<PaymentDto> read() {
        return paymentService.read();
    }
}
