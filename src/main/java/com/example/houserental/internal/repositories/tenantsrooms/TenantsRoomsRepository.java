package com.example.houserental.internal.repositories.tenantsrooms;

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

public interface TenantsRoomsRepository extends JpaRepository<TenantsRooms, Integer> {
    Page<TenantsRooms> findAll(Specification<TenantsRooms> specification, Pageable page);

    @Query("SELECT t FROM TenantsRooms t WHERE t.tenant.id = ?1")
    List<TenantsRooms> findTenantsRoomsByTenantId(int tenantId);

    @Modifying
    @Query("UPDATE TenantsRooms t SET t.status = 'Inactive' WHERE t.id = ?1")
    int updateTenantsRoomsStatusById(int id);

    @Modifying
    @Query(value = "UPDATE tenants_rooms SET tenant = :tenantID, room = :roomID, status = :status WHERE id = :id", nativeQuery = true)
    int updateTenantsRoomsById(@Param("id") int id, @Param("tenantID") int tenantID, @Param("roomID") int roomID, @Param("status") String status);

    TenantsRooms findById(int id);

    @Query("SELECT COUNT(t) FROM TenantsRooms t WHERE t.room.id = ?1")
    int countByRoomId(int id);

    @Modifying
    @Query(value = "INSERT INTO tenants_rooms (tenant, room, status) VALUES (:tenantId, :roomId, :status)", nativeQuery = true)
    int insertTenantsRooms(Integer tenantId, Integer roomId, String status);

    @Query(value = "DELETE tenants_rooms WHERE tenants_rooms.id = :id", nativeQuery = true)
    @Modifying
    int deleteTenantsRoomsById(int id);

    @Query("SELECT COUNT(t) FROM TenantsRooms t WHERE t.tenant.rentDate >= ?1 AND t.tenant.rentDate <= ?2 AND t.status = 'Active'")
    int countByRentDateBetween(Date startDate, Date endDate);

    @Query("SELECT COUNT(DISTINCT t.room.id) FROM TenantsRooms t WHERE t.tenant.rentDate >= ?1 AND t.tenant.rentDate <= ?2 AND t.status = 'Active'")
    int countDistinctRoomIdByRentDateBetween(Date startDate, Date endDate);
}
