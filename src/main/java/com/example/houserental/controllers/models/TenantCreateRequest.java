package com.example.houserental.controllers.models;


import com.example.houserental.internal.models.address.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreateRequest {
    private String name;
    private int roomId;
    private Date birthdate;
    private String gender;
    private String phone;
    private String email;
    private String idNumber;
    private Address permanentAddress;
    private String licensePlate;
    private Date rentDate;
    private String description;

}
