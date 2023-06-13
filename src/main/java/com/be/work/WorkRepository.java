package com.be.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<WorkEntity, Long> {
    @Query("SELECT w from WorkEntity w join fetch UserEntity u where w.employeeInfo.id = u.id and w.employeeInfo.id =:employeeId")
    WorkEntity findAllByEmployeeId(@Param("employeeId") Long id);

    @Query("SELECT w from WorkEntity w join fetch UserEntity u where w.employeeInfo.id = u.id")
    List<WorkEntity> findAllEmployee();
    @Query("delete from WorkEntity w where w.employeeInfo.id  =: employeeId")
    void deleteWorkEntityByEmployeeInfo_Id(@Param("employeeId") Long id);
    @Query("SELECT w FROM WorkEntity w join fetch UserEntity u where w.employeeInfo.id = u.id and w.workType =:workType")
    List<WorkEntity> findEmployeeByWorkType(@Param("workType")String workType);
}
