package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateInvoiceRequest {
    private String paymentMethod;
    private String status;
    private Date closingDate;
    private List<Facility> facilities;
}
