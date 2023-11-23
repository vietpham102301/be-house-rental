package com.example.houserental.internal.models.facility;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "facility_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int facility;
    private int room;
    @Column(name = "previous_index")
    private Integer previousIndex;
    private int usageNumber;
    private Float pricePerUnit;
    private Float price; // tien da tra cho facility nay
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "invoice_details")
    private int invoiceDetails;


    public FacilityHistory(int facility, int room, int usageNumber, Float pricePerUnit, Float price, int invoiceDetails, int previousIndex){
        this.facility = facility;
        this.usageNumber = usageNumber;
        this.pricePerUnit = pricePerUnit;
        this.price = price;
        this.room = room;
        this.createdAt = new Date();
        this.invoiceDetails = invoiceDetails;
        this.previousIndex = previousIndex;
    }
}
