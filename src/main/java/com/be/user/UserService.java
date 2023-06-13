package com.be.user;

import com.be.address.AddressEntity;
import com.be.address.AddressRepository;
import com.be.bill.BillRepository;
import com.be.dto.request.EditCustomerRequest;
import com.be.dto.request.EditEmployeeRequest;
import com.be.dto.response.CustomerInfoResponse;
import com.be.dto.response.EmployeeInfoResponse;
import com.be.work.WorkEntity;
import com.be.work.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    WorkRepository workRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    BillRepository billRepository;
    public void editEmployee(String id, EditEmployeeRequest employeeRequest){
        Long idUpdated = Long.parseLong(id);
        UserEntity user = userRepository.findById(idUpdated).orElseThrow();
        if(user != null){
            WorkEntity workEntity = workRepository.findAllByEmployeeId(user.getId());
            user.setName(employeeRequest.getName());
            user.setPhone(employeeRequest.getPhone());
            user.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
            workEntity.setWorkType(employeeRequest.getWorkType());
            userRepository.save(user);
            workRepository.save(workEntity);
        }
    }
    public void editCustomer(EditCustomerRequest customerRequest){
        UserEntity user = userRepository.findById(customerRequest.getId());
        if(user != null){
            AddressEntity address = addressRepository.findAllByCustomerInfo(customerRequest.getId());
            user.setName(customerRequest.getName());
            user.setPhone(customerRequest.getPhone());
            address.setRoomNumber(customerRequest.getRoomNumber());
            address.setDepartmentNumber(customerRequest.getDepartmentNumber());
            userRepository.save(user);
            addressRepository.save(address);
        }
    }
    public void deleteEmployee(String id){
        long idDeleted = Long.parseLong(id);
        userRepository.deleteById(idDeleted);
        workRepository.deleteWorkEntityByEmployeeInfo_Id(idDeleted);
    }
    public void deleteCustomer(String id){
        long idDeleted = Long.parseLong(id);
        userRepository.deleteById(idDeleted);
        addressRepository.deleteAddressEntityByCustomerInfo_Id(idDeleted);
    }
    public CustomerInfoResponse getCustomerInfo(long id){
        AddressEntity address = addressRepository.findAllByCustomerInfo(id);
        CustomerInfoResponse customerInfoResponse = new CustomerInfoResponse();
        customerInfoResponse.setEmail(address.getCustomerInfo().getEmail());
        customerInfoResponse.setPhone(address.getCustomerInfo().getPhone());
        customerInfoResponse.setName(address.getCustomerInfo().getName());
        customerInfoResponse.setDepartmentNumber(address.getDepartmentNumber());
        customerInfoResponse.setRoomNumber(address.getRoomNumber());
        return customerInfoResponse;
    }
    public EmployeeInfoResponse getEmployeeInfo(long id){
        WorkEntity work = workRepository.findAllByEmployeeId(id);
        EmployeeInfoResponse response = new EmployeeInfoResponse();
        response.setEmail(work.getEmployeeInfo().getEmail());
        response.setPhone(work.getEmployeeInfo().getPhone());
        response.setName(work.getEmployeeInfo().getName());
        response.setWorkType(work.getWorkType());
        return response;
    }


}
