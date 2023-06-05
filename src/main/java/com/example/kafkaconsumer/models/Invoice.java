package com.example.kafkaconsumer.models;

import lombok.Data;

//@Builder(builderClassName = "builder")
@Data
public class Invoice {
    private Long id;
    private Double netAmount;
    private Double taxAmount;
    private Double totalAmount;
    private String desc;
}

