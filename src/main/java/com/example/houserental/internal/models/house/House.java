package com.example.houserental.internal.models.house;

import com.example.houserental.controllers.models.HouseCreateRequest;
import com.example.houserental.internal.models.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "houses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToOne
    @JoinColumn(name = "address")
    private Address address;

    @Column(name = "establish_date")
    private Date establishDate;
    @Column(name = "total_room")
    private Integer totalRoom;
    private Integer manager;
    private String status;
    private String description;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;



    public static House convertToHouse(HouseCreateRequest houseCreateRequest){
        House house = new House( null,houseCreateRequest.getName(),
                Address.toAddress(houseCreateRequest.getAddress()), houseCreateRequest.getEstablishDate(),
                0, houseCreateRequest.getManager(),
                houseCreateRequest.getStatus(), houseCreateRequest.getDescription(), new Date());
        return house;
    }
}
