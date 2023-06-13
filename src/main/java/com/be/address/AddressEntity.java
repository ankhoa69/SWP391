package com.be.address;

import com.be.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private UserEntity customerInfo;
    private String departmentNumber;
    private String roomNumber;

    public AddressEntity(UserEntity customerInfo, String departmentNumber, String roomNumber) {
        this.customerInfo = customerInfo;
        this.departmentNumber = departmentNumber;
        this.roomNumber = roomNumber;
    }
}