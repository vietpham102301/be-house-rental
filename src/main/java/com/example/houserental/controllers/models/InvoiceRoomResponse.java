package com.example.houserental.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRoomResponse {
    private int id;
    private Date createdAt;
    private Float total;
    private String status;
}
