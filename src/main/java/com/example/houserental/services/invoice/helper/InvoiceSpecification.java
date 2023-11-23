package com.example.houserental.services.invoice.helper;


import com.example.houserental.internal.models.invoice.Invoice;
import com.example.houserental.internal.models.invoice.InvoiceDetail;
import com.example.houserental.internal.models.room.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class InvoiceSpecification {
    public static Specification<InvoiceDetail> withFilter(Date from, Integer houseId, String status) {
        return (root, query, criteriaBuilder) -> {
            Join<InvoiceDetail, Invoice> invoiceJoin = root.join("invoice");
            Join<InvoiceDetail, Room> roomJoin = root.join("room");
            Predicate predicate = criteriaBuilder.conjunction();


            if (from != null) {
                //join cai nao thi dung field cua entity do
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(invoiceJoin.get("createdAt"), from));
            }
            if (houseId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(roomJoin.get("house").get("id"), houseId));
            }
            if (status != null && !status.isEmpty()){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(invoiceJoin.get("status"), status));
            }
            query.orderBy(criteriaBuilder.desc(invoiceJoin.get("createdAt")));
            return predicate;
        };
    }

    public static Specification<InvoiceDetail> withKeywords(String keywords) {
        return (root, query, criteriaBuilder) -> {
            Join<InvoiceDetail, Invoice> invoiceJoin = root.join("invoice");
            Join<InvoiceDetail, Room> roomJoin = root.join("room");

            if(keywords == null || keywords.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keywords + "%";
            Predicate predicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(roomJoin.get("name")), likePattern.toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(invoiceJoin.get("tenant").get("name")), likePattern.toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(invoiceJoin.get("creator").get("name")), likePattern.toLowerCase())
            );

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return predicate;
        };
    }
    public static Specification<InvoiceDetail> withRoomId(int id) {
        return (root, query, criteriaBuilder) -> {
            Join<InvoiceDetail, Invoice> invoiceJoin = root.join("invoice");
            Join<InvoiceDetail, Room> roomJoin = root.join("room");

            Predicate predicate = criteriaBuilder.conjunction();
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(roomJoin.get("id"), id));

            query.orderBy(criteriaBuilder.desc(invoiceJoin.get("createdAt")));
            return predicate;
        };
    }
}
