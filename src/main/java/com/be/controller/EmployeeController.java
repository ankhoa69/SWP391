package com.be.controller;


import com.be.bill.BillService;
import com.be.dto.request.IdRequest;
import com.be.dto.response.ScheduleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {
    private final BillService billService;

    public EmployeeController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/info")
    public ResponseEntity<?> getBills(@RequestBody IdRequest requestInfo) {
        List<ScheduleResponse> list = billService.getScheduleByEmployee(requestInfo.getId());
        return ResponseEntity.ok(list);
    }
}
