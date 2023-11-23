package com.example.houserental.controllers.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityHistory {
    private Integer id;
    private String name;
    private Integer previousIndex;
    private Integer currentIndex;
    private Integer usage;
    private Float unitPrice;
    private String unit;
    private Float price;

}
