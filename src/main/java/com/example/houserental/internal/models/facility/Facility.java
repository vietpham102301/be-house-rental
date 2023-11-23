package com.example.houserental.internal.models.facility;

import com.example.houserental.internal.models.house.House;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String unit;
    private Float price; //this is unit price
    private int house;

    public Facility(String name, String unit, Float price, int house) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.house = house;
    }
}
