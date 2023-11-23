package com.example.houserental.services.room.helper;

import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.room.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {
    public static Specification<Room> withFilter(String houseName, Integer floor, String status) {
        return (root, query, criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (houseName != null && !houseName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("house").get("name"), houseName));
            }
            if (floor != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("floor"), floor));
            }
            if (status != null && !status.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
            }
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return predicate;
        };
    }

    public static Specification<Room> withSearchKeywords(String keywords){
        return (root, query, criteriaBuilder) -> {
            Join<Room, House> houseJoin = root.join("house");
            if(keywords == null || keywords.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keywords.toLowerCase() + "%";

            Predicate roomNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
            Predicate houseNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(houseJoin.get("name")), likePattern);
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
   
            return criteriaBuilder.or(roomNamePredicate, houseNamePredicate, descriptionPredicate);
        };
    }
}
