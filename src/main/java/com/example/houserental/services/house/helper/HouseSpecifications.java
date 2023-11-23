package com.example.houserental.services.house.helper;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.house.House;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HouseSpecifications {
    public static Specification<House> withAddressFilters(String city, String district, String ward, String status) {
        return (root, query, criteriaBuilder) -> {
            Join<House, Address> addressJoin = root.join("address");
            Predicate predicates = criteriaBuilder.conjunction();

            if (city != null && !city.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(addressJoin.get("city"), city));
            }

            if (district != null && !district.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(addressJoin.get("district"), district));
            }

            if (ward != null && !ward.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(addressJoin.get("ward"), ward));
            }

            if (status != null && !status.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("status"), status));
            }

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return predicates;
        };
    }
}
