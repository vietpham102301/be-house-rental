package com.example.houserental.controllers.models;

import com.example.houserental.Common.Message;
import com.example.houserental.internal.models.user.Role;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;
//import javax.validation.constraints.Email;

//import javax.validation.constraints.Pattern;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = Message.USERNAME_VALIDATE)
    @NotNull(message = "Username cannot be empty")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = Message.PASSWORD_VALIDATE)
    @NotNull(message = "Password cannot be empty")
    private String password;

    @Pattern(regexp = "^[a-zA-Z ]{1,50}$", message = Message.FIRSTNAME_VALIDATE)
    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Birthdate cannot be null")
    private Date birthdate;

    @Pattern(regexp = "(^$|[a-zA-Z]{2,6}$)", message = Message.GENDER_VALIDATE)
    @NotNull(message = "Gender cannot be empty")
    private String gender;

    @Pattern(regexp = "(^$|[0-9]{10,10}$)", message = Message.PHONE_VALIDATE)
    @NotNull(message = "Phone cannot be empty")
    private String phone;

    @Email(message = Message.EMAIL_VALIDATE)
    @NotNull(message = "Email cannot be empty")
    private String email;
    @Pattern(regexp = "(^$|[0-9]{12,12}$)", message = Message.ID_NUM_VALIDATE)
    @NotNull(message = "ID number cannot be empty")
    private String idNumber;



    @NotNull(message = "Started date cannot be null")
    private Date startedDate;

    @NotNull(message = "Description cannot be empty")
    private String description;

    @Enumerated
    private Role role;

    public void preProcessing() {
        this.username = username.toLowerCase().trim();
        this.password = password.trim();
        this.email = email.toLowerCase().trim();
        this.name = name.trim();
        this.gender = gender.trim();
    }
}
