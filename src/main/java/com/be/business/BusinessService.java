package com.be.business;

import com.be.dto.request.EditBusinessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public void updateBusinessPrice(EditBusinessRequest editBusinessRequest) {
        BusinessEntity businessEntity = businessRepository.findById(editBusinessRequest.getBusinessId()).orElseThrow();
        businessEntity.setPrice(editBusinessRequest.getPrice());
        businessRepository.save(businessEntity);
    }

    public BusinessEntity getBusinessInfo(long id) {
        return businessRepository.findById(id).orElseThrow();
    }
}
