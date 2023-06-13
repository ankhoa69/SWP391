package com.be.dto.response;

import lombok.Data;

@Data
public class BillInfoResponse {
    String workType;
    String day;
    String date;
    String month;
    String hour;
    String departmentNumber;
    String roomNumber;
    String customerPhone;
    boolean payStatus;
    boolean completedStatus;
    float total;
    int quantity;
}
