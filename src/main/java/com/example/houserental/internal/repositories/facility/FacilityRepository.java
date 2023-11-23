package com.example.houserental.internal.repositories.facility;

import com.example.houserental.internal.models.facility.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    List<Facility> findFacilitiesByHouse(int houseId);

    @Modifying
    @Query("UPDATE Facility f SET f.name = :name, f.unit = :unit, f.price = :price WHERE f.id = :id")
    int updateFacilityById(Integer id, String name, String unit, Float price);

}
