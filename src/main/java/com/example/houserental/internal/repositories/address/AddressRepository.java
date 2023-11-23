package com.example.houserental.internal.repositories.address;


import com.example.houserental.internal.models.address.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findAddressByCityAndDistrictAndWardAndStreetAndHouseNumber(String city, String district, String ward, String street, int houseNumber);
    @Modifying
    @Query("UPDATE Address a SET a.city = :city, a.district = :district, a.ward = :ward, a.street = :street, a.houseNumber = :houseNumber WHERE a.id = :id")
    int updateAddress(@Param("id") Integer id, @Param("city") String city, @Param("district") String district, @Param("ward") String ward, @Param("street") String street, @Param("houseNumber") int houseNumber);

    @Query("SELECT DISTINCT a.city FROM Address a")
    String[] getAllCities();

    @Query("SELECT DISTINCT a.district FROM Address a WHERE a.city = :city")
    String[] getAllDistricts(@Param("city") String city);

    @Query("SELECT DISTINCT a.ward FROM Address a WHERE a.city = :city AND a.district = :district")
    String[] getAllWards(@Param("city") String city, @Param("district") String district);
}
