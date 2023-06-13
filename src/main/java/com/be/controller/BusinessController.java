package com.be.controller;

import com.be.business.BusinessEntity;
import com.be.business.BusinessRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BusinessController {
    private final BusinessRepository businessRepository;

    public BusinessController(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @GetMapping("/services")
    public ResponseEntity<List<BusinessEntity>> getAllBusiness(){
        List<BusinessEntity> businessEntities = businessRepository.findAll();
        return ResponseEntity.ok(businessEntities);
    }

}
