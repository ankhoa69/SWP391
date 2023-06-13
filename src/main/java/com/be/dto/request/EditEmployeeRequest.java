package com.be.dto.request;

import lombok.Data;

@Data
public class EditEmployeeRequest {
    String name;
    String email;
    String phone;
    String password;
    String workType;
}
