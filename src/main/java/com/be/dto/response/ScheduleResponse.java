package com.be.dto.response;


import lombok.Data;

@Data

public class ScheduleResponse {
    String workType;
    String departmentNumber;
    String roomNumber;
    String customerPhone;
    boolean payStatus;
    boolean completedStatus;
    float total;
    int quantity;
    String hour;
}
