package com.example.houserental.internal.repositories.tenant;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Integer> {
    List<Tenant> findTenantByPhoneOrEmailOrIdNumberOrLicensePlate(String phone, String email, String idNumber, String licensePlate);


    @Query("UPDATE Tenant t SET t.name = :tenantName, t.birthdate = :birthDate, t.gender = :gender, t.phone = :phone, t.email = :email, t.idNumber = :idNumber, t.licensePlate = :licensePlate, t.rentDate = :rentDate, t.description = :description WHERE t.id = :id")
    @Modifying
    int updateTenantById(@Param("id") int id, @Param("tenantName") String tenantName, @Param("birthDate") Date birthDate,
                         @Param("gender") String gender, @Param("phone") String phone, @Param("email") String email,
                         @Param("idNumber") String idNumber,
                         @Param("licensePlate") String licensePlate, @Param("rentDate") Date rentDate,
                         @Param("description") String description);

    Tenant findTenantById(int id);


    @Query("SELECT t FROM Tenant t WHERE t.name LIKE %?1%"
            + " OR t.phone LIKE %?1%"
            + " OR t.gender LIKE %?1%"
            + " OR t.email LIKE %?1%"
            + " OR t.idNumber LIKE %?1%"
            + " OR t.licensePlate LIKE %?1%"
            + " OR t.permanentAddress.city LIKE %?1%"
            + " OR t.permanentAddress.district LIKE %?1%"
            + " OR t.permanentAddress.ward LIKE %?1%"
            + " OR t.permanentAddress.street LIKE %?1%"
            + " OR t.description LIKE %?1%")

    List<Tenant> searchTenantByKeywords(String keywords);

}
