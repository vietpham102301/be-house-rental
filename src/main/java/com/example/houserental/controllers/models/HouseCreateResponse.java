package com.example.houserental.controllers.models;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.house.House;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseCreateResponse  {
    private Integer id;
    private String name;
    private Address address;
    private Date establishDate;
    private Integer totalRoom;
    private Integer manager;
    private String status;
    private String description;

    public static HouseCreateResponse toResponse(House house) {
        HouseCreateResponse response = new HouseCreateResponse();
        response.setId(house.getId());
        response.setName(house.getName());
        response.setAddress(house.getAddress());
        response.setEstablishDate(house.getEstablishDate());
        response.setTotalRoom(house.getTotalRoom());
        response.setManager(house.getManager());
        response.setStatus(house.getStatus());
        response.setDescription(house.getDescription());
        return response;
    }
}
