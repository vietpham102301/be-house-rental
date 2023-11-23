package com.example.houserental.controllers.models;

import com.example.houserental.Common.Message;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseCreateRequest {

    @Pattern(regexp = "^[a-zA-Z0-9 ]{1,50}$", message = "name just contain 1-50 alphabetical characters!")
    private String name;

    @Valid
    private Address address;
    private List<HouseFacility> facilities;

    @Past(message = "establishDate must be a past date")
    @Temporal(TemporalType.DATE)
    private Date establishDate;

    @Positive(message = "managerID is positive integer")
    private Integer manager;

    @Pattern(regexp = "(Active|Inactive)", message = "status just can be Active or Inactive")
    private String status;
    private String description;
}
