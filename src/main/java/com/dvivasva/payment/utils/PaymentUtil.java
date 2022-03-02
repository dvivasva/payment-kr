package com.dvivasva.payment.utils;


import com.dvivasva.payment.dto.PaymentDto;
import com.dvivasva.payment.entity.Payment;
import org.springframework.beans.BeanUtils;

public final class PaymentUtil {
    private PaymentUtil() {
    }

    public static PaymentDto entityToDto(final Payment payment) {
        var paymentDto = new PaymentDto();
        BeanUtils.copyProperties(payment, paymentDto);
        return paymentDto;
    }
    public static Payment dtoToEntity(final PaymentDto paymentDto) {
        var entity = new Payment();
        BeanUtils.copyProperties(paymentDto, entity);
        return entity;
    }


}
