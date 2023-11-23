package com.example.houserental.controllers.models;
import com.example.houserental.internal.models.house.House;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseResponse {
    private Integer id;
    private String name;
    private Address address;
    private List<HouseFacility> facilities;
    private Date establishDate;
    private Integer totalRoom;
    private Integer manager;
    private String status;
    private String description;
    private Date createdAt;
    private List<Map<String, Object>> imageData;

    public static HouseResponse toHouseResponse(House house, List<HouseFacility> facilities, Integer manager, List<Map<String, Object>> imageResponse){
        Address address = new Address(house.getAddress().getId(),house.getAddress().getCity(),
                house.getAddress().getDistrict(),
                house.getAddress().getWard(), house.getAddress().getStreet(),
                house.getAddress().getHouseNumber());
        HouseResponse houseResponse = new HouseResponse(house.getId(), house.getName(), address, facilities,
                house.getEstablishDate(), house.getTotalRoom(), manager, house.getStatus(),
                house.getDescription(), house.getCreatedAt(), imageResponse);
        return houseResponse;
    }


}
