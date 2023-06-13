package com.be.work;

import com.be.bill.BillEntity;
import com.be.bill.BillRepository;
import com.be.dto.request.BusinessNameRequest;
import com.be.dto.response.EmployeeInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkService {
    @Autowired
    WorkRepository workRepository;
    @Autowired
    BillRepository billRepository;

    public List<EmployeeInfoResponse> getEmployeesByBusiness(BusinessNameRequest businessNameRequest) {
        List<WorkEntity> workEntities = workRepository.findEmployeeByWorkType(businessNameRequest.getBusinessName());
        List<EmployeeInfoResponse> responses = workEntities.stream()
                .map(workEntity -> {
                    EmployeeInfoResponse employeeInfo = new EmployeeInfoResponse();
                    List<BillEntity> billEntities = billRepository.findAllByEmployeeId(workEntity.getEmployeeInfo().getId());
                    employeeInfo.setName(workEntity.getEmployeeInfo().getName());
                    employeeInfo.setPhone(workEntity.getEmployeeInfo().getPhone());
                    employeeInfo.setEmail(workEntity.getEmployeeInfo().getEmail());
                    employeeInfo.setWorkType(workEntity.getWorkType());
                    List<String> hours = billEntities.stream()
                            .map(BillEntity::getHour)
                            .collect(Collectors.toList());
                    employeeInfo.setWorkHours(hours);
                    return employeeInfo;
                })
                .collect(Collectors.toList());
        return responses;
    }

}
