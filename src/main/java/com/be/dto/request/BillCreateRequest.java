package com.be.dto.request;

import lombok.Data;

@Data
public class BillCreateRequest {
    public long customerId;
    public long businessId;
    private String day;
    private int date;
    private int month;
    private float total;
    private String payment;
    private int quantity;
    private String frequency;
    private String hour;
}
