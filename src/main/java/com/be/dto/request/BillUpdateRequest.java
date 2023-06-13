package com.be.dto.request;

import lombok.Data;

@Data
public class BillUpdateRequest {
    long billId;
    long employeeId;
}
