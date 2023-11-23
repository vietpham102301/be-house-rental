package com.example.houserental.internal.repositories.invoice;

import com.example.houserental.controllers.models.UpdateInvoiceRequest;
import com.example.houserental.internal.models.invoice.Invoice;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Modifying
    @Query(value = "INSERT INTO invoices (tenant, creator, payment_method, status, closing_date, created_at) VALUES (:tenantId, :creatorId, :paymentMethod, :status, :closingDate, NOW())", nativeQuery = true)
    int insertInvoice(Integer tenantId, Integer creatorId, String paymentMethod, String status, Date closingDate);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer getLastInsertedId();

    Page<Invoice> findAll(Specification<Invoice> specification, Pageable pageable);


    @Modifying
    @Query("UPDATE Invoice i SET i.status = ?2 WHERE i.id = ?1")
    int updateInvoiceStatusById(int id, String status);

    @Modifying
    @Query(value = "UPDATE invoices i SET i.payment_method = :paymentMethod, i.status = :status, i.closing_date = :closingDate WHERE i.id = :id", nativeQuery = true)
    int updateInvoiceById(@Param("id") int id, @Param("paymentMethod") String paymentMethod, @Param("status") String status, @Param("closingDate") Date closingDate);

    Invoice findById(int id);

    @Modifying
    @Query("DELETE FROM Invoice i WHERE i.id = ?1")
    int deleteInvoiceById(int id);


}
