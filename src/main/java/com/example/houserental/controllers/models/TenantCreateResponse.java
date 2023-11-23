package com.example.houserental.controllers.models;
import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.tenant.Tenant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreateResponse {
    private Integer id;
    private String name;
    private Date birthdate;
    private String gender;
    private String phone;
    private String email;
    private String idNumber;
    private Address permanentAddress;
    private String licensePlate;
    private Date rentDate;
    private String description;
    private Date createdAt;

    public static TenantCreateResponse toResponse(Tenant tenant) {
        TenantCreateResponse response = new TenantCreateResponse();
        response.setId(tenant.getId());
        response.setName(tenant.getName());
        response.setBirthdate(tenant.getBirthdate());
        response.setGender(tenant.getGender());
        response.setPhone(tenant.getPhone());
        response.setEmail(tenant.getEmail());
        response.setIdNumber(tenant.getIdNumber());
        response.setPermanentAddress(tenant.getPermanentAddress());
        response.setLicensePlate(tenant.getLicensePlate());
        response.setRentDate(tenant.getRentDate());
        response.setDescription(tenant.getDescription());
        response.setCreatedAt(tenant.getCreatedAt());
        return response;
    }
}
