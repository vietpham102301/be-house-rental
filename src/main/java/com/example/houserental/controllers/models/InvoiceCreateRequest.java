package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceCreateRequest {
    private Integer tenantId;
    private Integer roomId;
    private Integer creatorId;
    private String paymentMethod;
    private Date closingDate;
    private List<Facility> facilities;
}
