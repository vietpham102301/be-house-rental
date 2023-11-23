package com.example.houserental.internal.repositories.room;

import com.example.houserental.internal.models.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findRoomById(Integer id);
    List<Room> findRoomsByHouseId(Integer houseId);

    List<Room> findAll(Specification<Room> specification);

    @Modifying
    @Query("update Room r set r.status = ?2 where r.id = ?1")
    int updateStatusById(int id, String status);

    @Modifying
    @Query(value="INSERT INTO rooms (name, house, floor, area, status, rent_fee, description, capacity, current_tenant, created_at) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    int insertRoom(String name, Integer houseId, Integer floor, Float area, String status, Float rentFee, String description, int capacity, int currentTenant, Date createdAt);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer getLastInsertedId();

    @Modifying
    @Query(value = "UPDATE rooms SET name = ?2, floor = ?3, area = ?4, status = ?5, rent_fee = ?6, description = ?7, capacity = ?8 WHERE id = ?1", nativeQuery = true)
    int updateRoomById(int id, String name, Integer floor, Float area, String status, Float rentFee, String description, int capacity);

    @Modifying
    @Query(value = "UPDATE rooms r SET r.current_tenant = ?2 WHERE r.id = ?1", nativeQuery = true)
    int updateCurrentTenantById(int id, int currentTenant);

}
