package com.example.houserental.controllers.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateRequest {
    private String name;
    private Integer floor;
    private Float area;
    private String status;
    private Float rentFee;
    private String description;
    private int capacity;
}
