package com.be.controller;

import com.be.bill.BillEntity;
import com.be.bill.BillRepository;
import com.be.bill.BillService;
import com.be.discount.DiscountEntity;
import com.be.discount.DiscountRepository;
import com.be.dto.request.BillCreateRequest;
import com.be.dto.request.DiscountRequest;
import com.be.dto.request.EditCustomerRequest;
import com.be.dto.request.IdRequest;
import com.be.dto.response.CustomerInfoResponse;
import com.be.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {
    @Autowired
    UserService userService;
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillService billService;

    @Autowired
    DiscountRepository discountRepository;
    @PutMapping("/edit")
    public ResponseEntity<?> updateCustomer(@RequestBody EditCustomerRequest editCustomerRequest) {
        userService.editCustomer(editCustomerRequest);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/info")
    public ResponseEntity<CustomerInfoResponse> getCustomerInfo(@RequestBody IdRequest idRequest) {
        CustomerInfoResponse getCustomerDTO = userService.getCustomerInfo(idRequest.getId());
        return ResponseEntity.ok(getCustomerDTO);
    }

    @PostMapping("/order")
    public ResponseEntity<?> createBill(@RequestBody List<BillCreateRequest> billCreateRequest) {
        Stream<BillEntity> billEntity = billService.createOrder(billCreateRequest);
        return ResponseEntity.ok(billEntity);
    }

    @PostMapping("/orders")
    public ResponseEntity<List<BillEntity>> getCustomerBills(@RequestBody IdRequest requestInfo) {
        List<BillEntity> list = billRepository.findAllByCustomerId(requestInfo.getId());
        return ResponseEntity.ok(list);
    }
    @PostMapping("/discounts")
    public ResponseEntity<DiscountEntity> getDiscount(@RequestBody DiscountRequest discountRequest){
        DiscountEntity discount = discountRepository.findDiscountEntityByCode(discountRequest.getCode());
        return ResponseEntity.ok(discount);
    }
}
