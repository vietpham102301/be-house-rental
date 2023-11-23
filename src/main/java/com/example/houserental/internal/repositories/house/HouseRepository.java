package com.example.houserental.internal.repositories.house;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.house.House;
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


public interface HouseRepository extends JpaRepository<House, Integer> {

    House findById(int id);

    @Query("SELECT h FROM House h WHERE Lower(h.name) LIKE %?1%"
            + " OR Lower(h.address.city) LIKE %?1%"
            + " OR Lower(h.address.district) LIKE %?1%"
            + " OR Lower(h.address.ward)  LIKE %?1%"
            + " OR Lower(h.address.street) LIKE %?1%"
            + " OR Lower(h.description) LIKE %?1%")
    List<House> searchHouses(String keywords);

    @Query(value = "SELECT h FROM House h WHERE h.status <> 'inactive'")
    Page<House> findAll(Pageable page);

    House findHouseByName(String name);

    List<House> findHousesByManager (Integer manager);

    List<House> findAll(Specification<House> specification);
    @Modifying
    @Query("UPDATE House h SET h.status = 'Inactive' WHERE h.id = ?1")
    int updateHouseStatusById(Integer houseId);

    @Modifying
    @Query("UPDATE House h SET h.totalRoom = :totalRoom WHERE h.id = :id")
    int updateHouseTotalRoomById(@Param("id") Integer houseId, @Param("totalRoom") Integer totalRoom);

    @Modifying
    @Query("UPDATE House h SET h.name = :name, h.address = :address, h.establishDate = :establishDate, h.manager = :manager, h.status = :status, h.description = :description WHERE h.id = :id")
    int updateHouseById(@Param("id") Integer houseId, @Param("name") String name, @Param("address") Address address, @Param("establishDate") Date establishDate, @Param("manager") Integer manager, @Param("status") String status, @Param("description") String description);
}
