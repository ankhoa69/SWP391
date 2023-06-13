package com.be.bill;

import com.be.business.BusinessEntity;
import com.be.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bill")
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private UserEntity employee;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private BusinessEntity business;
    private String day;
    private int date;
    private int month;
    private String hour;
    private boolean completeStatus;
    private float total;
    private String payment;
    private boolean payStatus;
    private int quantity;
    private String frequency;
}