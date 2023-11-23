package com.example.houserental.controllers.models;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantInforResponse {
    private int id;
    private int tenantRoomId;
    private String tenantName;
    private String houseName;
    private Integer roomId;
    private String roomName;
    private Date birthDate;
    private String gender;
    private String phone;
    private String email;
    private String idNumber;
    private Address permanentAddress;
    private String licensePlates;
    private Date rentDate;
    private String status;
    private String description;
    private List<Map<String, Object>> imageData;
    private Date createdAt;

    public static TenantInforResponse toResponse(TenantsRooms tenantsRooms){
        TenantInforResponse res = new TenantInforResponse();
        res.setId(tenantsRooms.getTenant().getId());
        res.setTenantRoomId(tenantsRooms.getId());
        res.setTenantName(tenantsRooms.getTenant().getName());
        res.setHouseName(tenantsRooms.getRoom().getHouse().getName());
        res.setRoomId(tenantsRooms.getRoom().getId());
        res.setBirthDate(tenantsRooms.getTenant().getBirthdate());
        res.setGender(tenantsRooms.getTenant().getGender());
        res.setPhone(tenantsRooms.getTenant().getPhone());
        res.setEmail(tenantsRooms.getTenant().getEmail());
        res.setIdNumber(tenantsRooms.getTenant().getIdNumber());
        res.setPermanentAddress(tenantsRooms.getTenant().getPermanentAddress());
        res.setLicensePlates(tenantsRooms.getTenant().getLicensePlate());
        res.setRentDate(tenantsRooms.getTenant().getRentDate());
        res.setStatus(tenantsRooms.getStatus());
        res.setDescription(tenantsRooms.getTenant().getDescription());
        res.setRoomName(tenantsRooms.getRoom().getName());
        res.setCreatedAt(tenantsRooms.getTenant().getCreatedAt());
        return res;
    }
}
