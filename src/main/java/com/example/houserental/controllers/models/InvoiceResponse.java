package com.example.houserental.controllers.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class InvoiceResponse {
    private int id;
    private String tenantName;
    private String tenantEmail;
    private String roomName;
    private String houseName;
    private String creatorName;
    private String paymentMethod;
    private Date closingDate;
    private String status;
    private InvoiceDetail invoiceDetails;
    private Float totalCharge;
    private Date createdAt;
    private List<Map<String, Object>> imageData;
}
