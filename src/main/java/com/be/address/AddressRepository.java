package com.be.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    @Query("SELECT a from AddressEntity AS a JOIN UserEntity as u WHERE a.customerInfo.id = u.id AND a.customerInfo.id = :customerId")
    AddressEntity findAllByCustomerInfo(@Param("customerId") long id);
    @Query("SELECT a from AddressEntity AS a JOIN UserEntity as u WHERE a.customerInfo.id = u.id")
    List<AddressEntity> findAllCustomer();
    @Query("DELETE FROM AddressEntity a WHERE a.customerInfo.id = :customerId")
    void deleteAddressEntityByCustomerInfo_Id(@Param("customerId") long id);
}
