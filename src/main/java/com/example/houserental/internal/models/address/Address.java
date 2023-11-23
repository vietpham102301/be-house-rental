package com.example.houserental.internal.models.address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city;

    private String district;

    private String ward;

    private String street;

    @Column(name ="house_number")
    private int houseNumber;

    public static Address toAddress(com.example.houserental.controllers.models.Address other){
        return new Address(other.getId(), other.getCity(), other.getDistrict(), other.getWard(), other.getStreet(), other.getHouseNumber());
    }

}
