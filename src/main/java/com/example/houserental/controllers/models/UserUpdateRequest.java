package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String name;
    private String username;
    private String role;
    private Date birthDate;
    private String gender;
    private String email;
    private String phone;
    private String idNumber;
    private Date startedDate;
    private String status;
    private String description;
    private String password;
}
