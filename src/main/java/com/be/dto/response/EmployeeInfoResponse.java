package com.be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeInfoResponse {
    String name;
    String phone;
    String email;
    String workType;
    List<String> workHours;
}
