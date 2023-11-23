package com.example.houserental.controllers.models;

import com.example.houserental.internal.models.user.Role;
import com.example.houserental.internal.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInforResponse {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private String status;
    private List<String> houseNames;
    private Date birthDate;
    private String gender;
    private Date startedDate;
    private String idNumber;
    private String description;
    private List<Map<String, Object>> imageData;

    public UserInforResponse(User user, List<String> houseName, List<Map<String, Object>> imageData){
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.houseNames = houseName;
        this.birthDate = user.getBirthdate();
        this.gender = user.getGender();
        this.startedDate = user.getStartedDate();
        this.idNumber = user.getIdNumber();
        this.description = user.getDescription();
        this.imageData = imageData;
    }
}
