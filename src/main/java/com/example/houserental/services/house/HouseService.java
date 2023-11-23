package com.example.houserental.services.house;

import com.example.houserental.controllers.models.HouseCreateResponse;
import com.example.houserental.controllers.models.HouseFacility;
import com.example.houserental.controllers.models.HouseResponse;
import com.example.houserental.internal.models.house.House;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;



import java.util.List;


public interface HouseService {
    ResponseEntity<HouseCreateResponse> createHouse(House house, List<HouseFacility> facilities);

    Page<HouseResponse> listHouseWithFilter(int pageNumber, int pageSize, String city, String district, String ward, String status);
    boolean removeHouseById(int houseId);
    House getHouseById(int id);
    House updateHouseById(int id, House houseUpdateRequest, List<HouseFacility> facilities);
    Page<HouseResponse> searchHouseByKeywords(String keywords, int page, int size);
}
