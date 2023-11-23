package com.example.houserental.internal.models.tenant;

import com.example.houserental.controllers.models.TenantCreateRequest;
import com.example.houserental.internal.models.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date birthdate;
    private String gender;
    private String phone;
    private String email;
    @Column(name = "id_number")
    private String idNumber;

    @OneToOne
    @JoinColumn(name = "permanent_address")
    private Address permanentAddress;

    @Column(name="license_plate")
    private String licensePlate;
    @Column(name = "rent_date")
    private Date rentDate;
    private String description;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    public static Tenant convertToTenant(TenantCreateRequest request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setBirthdate(request.getBirthdate());
        tenant.setGender(request.getGender());
        tenant.setPhone(request.getPhone());
        tenant.setEmail(request.getEmail());
        tenant.setIdNumber(request.getIdNumber());
        tenant.setPermanentAddress(request.getPermanentAddress());
        tenant.setLicensePlate(request.getLicensePlate());
        tenant.setRentDate(request.getRentDate());
        tenant.setDescription(request.getDescription());
        tenant.setCreatedAt(new Date());

        return tenant;
    }

}
