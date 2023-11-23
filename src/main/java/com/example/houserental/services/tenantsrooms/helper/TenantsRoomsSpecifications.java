package com.example.houserental.services.tenantsrooms.helper;

import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TenantsRoomsSpecifications {

    public static Specification<TenantsRooms> withHouseRoomStatusFilters(String houseName, String roomName, String status) {
        return (root, query, criteriaBuilder) -> {
            Join<TenantsRooms, Tenant> tenantJoin = root.join("tenant");
            Join<TenantsRooms, Room> roomJoin = root.join("room");
            Join<Room, House> houseJoin = roomJoin.join("house");

            Predicate predicate = criteriaBuilder.conjunction();

            if (houseName != null && !houseName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(houseJoin.get("name"), houseName));
            }

            if (roomName != null && !roomName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(roomJoin.get("name"), roomName));
            }

            if (status != null && !status.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
            }
            query.orderBy(criteriaBuilder.desc(tenantJoin.get("createdAt")));
            return predicate;
        };
    }

    public static Specification<TenantsRooms> withKeywords(String keywords){
        return (root, query, criteriaBuilder) -> {
            Join<TenantsRooms, Tenant> tenantJoin = root.join("tenant");
            Join<Tenant, Address> addressJoin = tenantJoin.join("permanentAddress");
            Join<TenantsRooms, Room> roomJoin = root.join("room");
            Join<Room, House> houseJoin = roomJoin.join("house");
            if(keywords == null || keywords.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keywords + "%";

            Predicate tenantNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("name")), likePattern.toLowerCase());
            Predicate roomNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(roomJoin.get("name")), likePattern);
            Predicate houseNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(houseJoin.get("name")), likePattern);
            Predicate addressCityPredicate = criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get("city")), likePattern);
            Predicate addressDistrictPredicate = criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get("district")), likePattern);
            Predicate addressWardPredicate = criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get("ward")), likePattern);
            Predicate addressStreetPredicate = criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get("street")), likePattern);
            Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("phone")), likePattern);
            Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("email")), likePattern);
            Predicate idCardPredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("idNumber")), likePattern);
            Predicate licensePlatePredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("licensePlate")), likePattern);
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(tenantJoin.get("description")), likePattern);
//            query.orderBy(criteriaBuilder.desc(tenantJoin.get("createdAt")));
            return criteriaBuilder.or(tenantNamePredicate, roomNamePredicate, houseNamePredicate, addressCityPredicate,
                    addressDistrictPredicate, addressWardPredicate, addressStreetPredicate
                    , phonePredicate, emailPredicate, idCardPredicate,
                    licensePlatePredicate, descriptionPredicate);
        };
    }
}