package com.be.controller;

import com.be.address.AddressEntity;
import com.be.address.AddressRepository;
import com.be.auth.AuthenticationService;
import com.be.bill.BillEntity;
import com.be.bill.BillService;
import com.be.business.BusinessEntity;
import com.be.business.BusinessService;
import com.be.dto.request.*;
import com.be.dto.response.EmployeeInfoResponse;
import com.be.user.UserService;
import com.be.work.WorkEntity;
import com.be.work.WorkRepository;
import com.be.work.WorkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AddressRepository addressRepository;
    private final WorkService workService;
    private final UserService userService;
    private final WorkRepository workRepository;
    private final AuthenticationService authenticationService;
    private final BillService billService;
    private final BusinessService businessService;

    public AdminController(AddressRepository addressRepository, WorkService workService, UserService userService, WorkRepository workRepository, AuthenticationService authenticationService, BillService billService, BusinessService businessService) {
        this.addressRepository = addressRepository;
        this.workService = workService;
        this.userService = userService;
        this.workRepository = workRepository;
        this.authenticationService = authenticationService;
        this.billService = billService;
        this.businessService = businessService;
    }


    //    @GetMapping("/employees/{id}")
//    public ResponseEntity<EmployeeInfoResponse> getEmployeeInfo(@PathVariable String id) {
//        long employeeId = Long.parseLong(id);
//        EmployeeInfoResponse infoResponse =  userService.getEmployeeInfo(employeeId);
//        return ResponseEntity.ok(infoResponse);
//    }
//    @GetMapping("/customers/{id}")
//    public ResponseEntity<CustomerInfoResponse> getCustomerInfo(@PathVariable String id) {
//        long customerId = Long.parseLong(id);
//        CustomerInfoResponse getCustomerDTO = userService.getCustomerInfo(customerId);
//        return ResponseEntity.ok(getCustomerDTO);
//    }
    @GetMapping("/business/{id}")
    public ResponseEntity<BusinessEntity> getBusinessInfo(@PathVariable String id) {
        long businessId = Long.parseLong(id);
        BusinessEntity businessEntity = businessService.getBusinessInfo(businessId);
        return ResponseEntity.ok(businessEntity);
    }
    @PostMapping("/business")
    public ResponseEntity<List<EmployeeInfoResponse>> getEmployeesByBusiness(@RequestBody BusinessNameRequest businessNameRequest) {
        List<EmployeeInfoResponse> employeeInfo = workService.getEmployeesByBusiness(businessNameRequest);
        return ResponseEntity.ok(employeeInfo);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<AddressEntity>> getCustomers() {
        List<AddressEntity> list = addressRepository.findAllCustomer();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<WorkEntity>> getEmployees() {
        List<WorkEntity> list = workRepository.findAllEmployee();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> editEmployee(@PathVariable String id, @RequestBody EditEmployeeRequest employeeRequest) {
        userService.editEmployee(id, employeeRequest);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/customers")
    public ResponseEntity<?> editCustomer(@RequestBody EditCustomerRequest customerRequest) {
        userService.editCustomer(customerRequest);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        userService.deleteEmployee(id);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        userService.deleteCustomer(id);
        return ResponseEntity.ok(null);
    }
    @DeleteMapping("/cancel-order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id){
        billService.deleteBill(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/create-employee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRegisterRequest request) {
        authenticationService.createEmployee(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/assign")
    public ResponseEntity<BillEntity> assignEmployee(@RequestBody BillUpdateRequest billUpdateRequest) {
        BillEntity bill = billService.assignEmployee(billUpdateRequest);
        return ResponseEntity.ok(bill);
    }

    @PutMapping("/confirm/{billId}")
    public ResponseEntity<BillEntity> confirm(@PathVariable String billId) {
        long id = Long.parseLong(billId);
        BillEntity bill = billService.confirm(id);
        return ResponseEntity.ok(bill);
    }

    @PutMapping("/update-service")
    public ResponseEntity<?> updateBusinessPrice(@RequestBody EditBusinessRequest editBusinessRequest) {
        businessService.updateBusinessPrice(editBusinessRequest);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<BillEntity>> getAllBills() {
        List<BillEntity> listBill = billService.getAllBills();
        return ResponseEntity.ok(listBill);
    }


}
