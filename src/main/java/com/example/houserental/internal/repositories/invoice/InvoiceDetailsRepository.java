package com.example.houserental.internal.repositories.invoice;

import com.example.houserental.internal.models.invoice.InvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Date;
import java.util.List;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetail, Integer> {
    @Modifying
    @Query(value = "INSERT INTO invoice_details (invoice, room, rent_fee, created_at) VALUES (:invoiceId, :roomId, :rentFee, NOW())", nativeQuery = true)
    int insertInvoiceDetails(Integer invoiceId, Integer roomId, Float rentFee);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer getLastInsertedId();

    List<InvoiceDetail> findAll(Specification<InvoiceDetail> specification);

    @Modifying
    @Query(value = "DELETE FROM invoice_details id WHERE id.invoice = ?1", nativeQuery = true)
    int deleteByInvoiceId(int Id);


    @Query(value="SELECT * FROM invoice_details WHERE invoice = ?1", nativeQuery = true)
    InvoiceDetail findByInvoiceId(int id);


    @Modifying
    @Query(value = "UPDATE invoice_details id SET id.room = :roomId, id.rent_fee = :rentFee WHERE id.invoice = :id", nativeQuery = true)
    int updateInvoiceDetailsByInvoiceId(int id, int roomId, float rentFee);

    @Query("SELECT id FROM InvoiceDetail id WHERE id.createdAt >= ?1 AND id.createdAt <= ?2 AND id.invoice.status = 'PAID'")
    List<InvoiceDetail> findAllByCreatedAtBetween(Date startDate, Date endDate);
}
