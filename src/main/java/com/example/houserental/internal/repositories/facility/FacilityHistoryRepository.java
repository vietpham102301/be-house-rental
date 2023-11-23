package com.example.houserental.internal.repositories.facility;

import com.example.houserental.internal.models.facility.FacilityHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FacilityHistoryRepository extends JpaRepository<FacilityHistory, Integer> {
    @Query(value = "SELECT * FROM facility_history WHERE facility = :facilityId AND room = :roomId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    FacilityHistory findLatestByFacilityId(@Param("facilityId") int facilityId, @Param("roomId") int roomId);

    FacilityHistory findByInvoiceDetailsAndFacility(int invoiceDetails, int facility);

    List<FacilityHistory> findByInvoiceDetails(int invoiceDetails);

    @Modifying
    @Query("UPDATE FacilityHistory fh SET fh.usageNumber = :usageNumber, fh.price=:price WHERE fh.invoiceDetails = :invoiceDetailsId and fh.facility = :facilityId")
    int updateFacilityUsageNumberByInvoiceDetailsAndFacility(@Param("invoiceDetailsId")int invoiceDetailsId, @Param("facilityId") int facilityId, @Param("usageNumber") int usageNumber, @Param("price") Float price);

    @Modifying
    @Query(value = "UPDATE facility_history fh SET fh.previous_index = :previousIndex, fh.price=:price WHERE fh.id = :id", nativeQuery = true)
    int updateFacilityHistoriesPreviousIndexById(@Param("id") int id, @Param("previousIndex") int previousIndex, @Param("price") Float price);

    @Query("SELECT fh FROM FacilityHistory fh WHERE fh.createdAt > :createdAt AND fh.facility =:facilityId ORDER BY fh.createdAt ASC LIMIT 1")
    FacilityHistory findNearestRecord(@Param("createdAt") Date createdAt, @Param("facilityId") int facilityId);


    @Modifying
    @Query("DELETE FROM FacilityHistory fh WHERE fh.invoiceDetails = ?1")
    int deleteByInvoiceDetailsId(int Id);

    @Query("SELECT SUM(fh.price) FROM FacilityHistory fh WHERE fh.invoiceDetails = ?1 AND fh.createdAt >= ?2 AND fh.createdAt <= ?3")
    Float sumPriceByInvoiceDetailsAndCreatedAtBetween(int invoiceDetailsId, Date startDate, Date endDate);
}
