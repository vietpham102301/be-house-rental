package com.example.houserental.controllers.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponse {
    private int id;
    private String name;
    private Integer previousIndex;
    private Float unitPrice;
    private String unit;
}
