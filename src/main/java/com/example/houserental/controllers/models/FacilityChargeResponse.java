package com.example.houserental.controllers.models;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityChargeResponse {

    List<FacilityCharge> facilityCharges;
    Float total;
}
