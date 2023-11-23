package com.example.houserental.controllers.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityCharge {
    private int id;
    private String name;
    private Integer currentIndex;
    private Integer usage;
    private Float totalPrice;
}
