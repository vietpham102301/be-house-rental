package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {
    private String name;
    private int houseId;
    private Integer floor;
    private Float area;
    private String status;
    private Float rentFee;
    private String description;
    private int capacity;
    List<Service> services;
}
