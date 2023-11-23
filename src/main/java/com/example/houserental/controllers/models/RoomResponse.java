package com.example.houserental.controllers.models;

import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.models.room.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoomResponse {
    private int id;
    private String name;
    private String houseName;
    private String status;
    private Integer floor;
    private Float area;
    private int currentTenant;
    private int capacity;
    private Float rentFee;
    private String description;
    private List<String> services;
    private List<Map<String, Object>> imageData;

    public static RoomResponse toRoomResponse(Room room, List<Service> services, List<Map<String, Object>> imageData) {
        List<String> serviceNames = new ArrayList<>();
        for(Service service : services) {
            serviceNames.add(service.getName());
        }
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getHouse().getName(),
                room.getStatus(),
                room.getFloor(),
                room.getArea(),
                room.getCurrentTenant(),
                room.getCapacity(),
                room.getRentFee(),
                room.getDescription(),
                serviceNames,imageData
        );
    }
}
