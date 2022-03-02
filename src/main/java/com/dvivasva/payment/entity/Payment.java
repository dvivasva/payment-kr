package com.dvivasva.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Payment")
public class Payment implements Serializable {
    @Id
    private String id;
    private double amount;
    private String numberPhoneOrigin;
    private String numberPhoneDestination;
    private Date date;
}
