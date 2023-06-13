package com.be.bill;

import com.be.address.AddressEntity;
import com.be.address.AddressRepository;
import com.be.business.BusinessEntity;
import com.be.business.BusinessRepository;
import com.be.dto.request.BillCreateRequest;
import com.be.dto.request.BillUpdateRequest;
import com.be.dto.response.ScheduleResponse;
import com.be.user.UserEntity;
import com.be.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BillService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    AddressRepository addressRepository;

    public void deleteBill(String id) {
        try {
            long idDeleted = Long.parseLong(id);
            billRepository.deleteById(idDeleted);
        } catch (NumberFormatException e) {

        }

    }

    public BillEntity assignEmployee(BillUpdateRequest billUpdateRequest) {
        UserEntity user = userRepository.findById(billUpdateRequest.getEmployeeId());
        BillEntity billEntity = billRepository.findById(billUpdateRequest.getBillId()).orElseThrow();
        billEntity.setEmployee(user);
        billRepository.save(billEntity);
        return billEntity;
    }

    public BillEntity confirm(long id) {
        BillEntity billEntity = billRepository.findById(id).orElseThrow();
        billEntity.setCompleteStatus(true);
        billEntity.setPayStatus(true);
        return billEntity;
    }

    public List<BillEntity> getAllBills() {
        return billRepository.findAll();
    }

    public Stream<BillEntity> createOrder(List<BillCreateRequest> billCreateRequest) {
        return billCreateRequest.stream().map(request -> {
            BillEntity billEntity = new BillEntity();
            BusinessEntity businessEntity = businessRepository.findById(request.getBusinessId()).orElseThrow();
            billEntity.setBusiness(businessEntity);
            billEntity.setCompleteStatus(false);
            UserEntity customer = userRepository.findById(request.getCustomerId());
            billEntity.setCustomer(customer);
            billEntity.setHour(request.getHour());
            billEntity.setDate(request.getDate());
            billEntity.setDay(request.getDay());
            billEntity.setMonth(request.getMonth());
            billEntity.setTotal(request.getTotal());
            billEntity.setPayment(request.getPayment());
            billEntity.setQuantity(request.getQuantity());
            billEntity.setFrequency(request.getFrequency());
            billEntity.setPayStatus(request.getPayment().equalsIgnoreCase("paypal"));
            billEntity.setCompleteStatus(false);
            billRepository.save(billEntity);
            return billEntity;
        });
    }


    public List<ScheduleResponse> getScheduleByEmployee(long id) {
        List<BillEntity> billEntityList = billRepository.findAllByEmployeeId(id);
        LocalDate current = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        String currentDate = current.format(formatter);
        int currentDayOfMonth = current.getDayOfMonth();
        return billEntityList.stream()
                .filter(billEntity -> (billEntity.getFrequency().equals("Weekly") && billEntity.getDay().equals(currentDate))
                        || (billEntity.getFrequency().equals("Monthly") && billEntity.getDate() == currentDayOfMonth))
                .map(billEntity -> {
                    AddressEntity address = addressRepository.findAllByCustomerInfo(billEntity.getCustomer().getId());
                    ScheduleResponse scheduleResponse = new ScheduleResponse();
                    scheduleResponse.setWorkType(billEntity.getBusiness().getName());
                    scheduleResponse.setCustomerPhone(billEntity.getCustomer().getPhone());
                    scheduleResponse.setCompletedStatus(billEntity.isCompleteStatus());
                    scheduleResponse.setPayStatus(billEntity.isPayStatus());
                    scheduleResponse.setTotal(billEntity.getTotal());
                    scheduleResponse.setDepartmentNumber(address.getDepartmentNumber());
                    scheduleResponse.setRoomNumber(address.getRoomNumber());
                    scheduleResponse.setQuantity(billEntity.getQuantity());
                    scheduleResponse.setHour(billEntity.getHour());
                    return scheduleResponse;
                })
                .collect(Collectors.toList());
    }

}
