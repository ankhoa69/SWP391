package com.be.bill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {
    @Query("SELECT b FROM BillEntity b WHERE b.customer.id = :customerId AND b.completeStatus = false")
    List<BillEntity> findAllByCustomerId(@Param("customerId") long id);
    @Query("SELECT b FROM BillEntity b WHERE b.employee.id = :employeeId AND b.completeStatus = false")
    List<BillEntity> findAllByEmployeeId(@Param("employeeId") long id);
}
