package com.example.houserental.controllers.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.aspectj.bridge.IMessage;


@Data
@AllArgsConstructor
public class Address {
    @NonNull
    private int id;
    @Pattern(regexp = "^[A-Za-z0-9 ]{1,50}$", message = "city just contain 1-50 alphabetical characters!")
    private String city;
    @Pattern(regexp = "^[A-Za-z0-9 ]{1,50}$", message = "district just contain 1-50 alphabetical characters!")
    private String district;
    @Pattern(regexp = "^^[A-Za-z0-9 ]{1,50}$", message = "ward just contain 1-50 alphabetical characters!")
    private String ward;
    @Pattern(regexp = "^^[A-Za-z0-9 ]{1,50}$", message = "street just contain 1-50 alphabetical characters!")
    private String street;
    @Column(name ="house_number")
    @Positive(message = "houseNumber is positive integer")
    private int houseNumber;

}