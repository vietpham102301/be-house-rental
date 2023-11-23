package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetail {
    List<FacilityHistory> facilityHistories;
    private Float totalFacilitiesPrice;
}
