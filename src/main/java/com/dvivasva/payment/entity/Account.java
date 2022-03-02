package com.dvivasva.payment.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
public class Account implements Serializable {
    private String id;
    private String number;
    private double availableBalance;

}
