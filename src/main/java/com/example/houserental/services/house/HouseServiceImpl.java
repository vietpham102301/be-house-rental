package com.example.houserental.services.house;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.HouseCreateResponse;
import com.example.houserental.controllers.models.HouseFacility;
import com.example.houserental.controllers.models.HouseResponse;
import com.example.houserental.controllers.models.RoomResponse;
import com.example.houserental.exception.house.HouseAlreadyExistException;
import com.example.houserental.exception.house.HouseException;
import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.facility.Facility;
import com.example.houserental.internal.models.house.House;

import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.repositories.address.AddressRepository;
import com.example.houserental.internal.repositories.facility.FacilityRepository;
import com.example.houserental.internal.repositories.house.HouseRepository;
import com.example.houserental.internal.repositories.room.RoomRepository;
import com.example.houserental.internal.repositories.user.UserRepository;
import com.example.houserental.services.house.helper.HouseSpecifications;
import com.example.houserental.services.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService{

    private final HouseRepository houseRepository;
    private final AddressRepository addressRepository;
    private final RoomRepository roomRepository;
    private final FacilityRepository facilityRepository;

    private final ImageService imageService;


    @Override
    @Transactional
    public ResponseEntity<HouseCreateResponse> createHouse(House house, List<HouseFacility> facilities) {
            Address address = house.getAddress();
            Address existAddress = addressRepository.findAddressByCityAndDistrictAndWardAndStreetAndHouseNumber(address.getCity(), address.getDistrict(), address.getWard(), address.getStreet(), address.getHouseNumber());
            House existHouseName = houseRepository.findHouseByName(house.getName());
            if(existHouseName != null){
                throw new HouseException(Message.HOUSE_NAME_EXIST);
            }
            if (existAddress == null){
                address = addressRepository.save(address);
                house.setAddress(address);
                House insertedHouse = houseRepository.save(house);
                for(HouseFacility facility: facilities){
                    facilityRepository.save(new Facility(facility.getName(), facility.getUnit(), facility.getPrice(), insertedHouse.getId()));
                }
                return new ResponseEntity<>(HouseCreateResponse.toResponse(house), HttpStatus.CREATED);
            }else{
                throw new HouseAlreadyExistException("This house address is already taken");
            }
    }



    @Override
    public Page<HouseResponse> listHouseWithFilter(int pageNumber, int pageSize, String city, String district, String ward, String status) {
        List<House> filteredHouses = houseRepository.findAll(
                HouseSpecifications.withAddressFilters(city, district, ward, status));

        return getHouseResponses(pageNumber, pageSize, filteredHouses);
    }

    Page<HouseResponse> getHouseResponse(Pageable pageable, List<HouseResponse> houseResponseList){
        int start = (int)pageable.getOffset();
        int end = Math.min(start +pageable.getPageSize(), houseResponseList.size());
        final Page<HouseResponse> response = new PageImpl<>(houseResponseList.subList(start, end), pageable, houseResponseList.size());
        return response;
    }

    @Override
    @Transactional
    public boolean removeHouseById(int id) {
        House house = houseRepository.findById(id);
        if(house != null) {
            if(!house.getStatus().equals("Inactive")) {
                List<Room> rooms = roomRepository.findRoomsByHouseId(id);
                for(Room room: rooms){
                   roomRepository.updateStatusById(room.getId(), "Inactive");
                }
                int updatedRow = houseRepository.updateHouseStatusById(id);
                return updatedRow >0;
            }else {
                throw new HouseException(Message.HOUSE_INACTIVE);
            }
        }else{
            throw new HouseException("House is not found");
        }

    }

    @Override
    public House getHouseById(int id) {
        House house = houseRepository.findById(id);
        if(house == null){
            throw new HouseException("House is not found");
        }
        return house;
    }

    @Override
    @Transactional
    public House updateHouseById(int id, House houseUpdateRequest, List<HouseFacility> facilities) {

            House house = houseRepository.findById(id);
            if(house == null){
                throw new HouseException(Message.HOUSE_NOTFOUND);
            }
            if(houseUpdateRequest.getAddress().getId() != house.getAddress().getId()){
                throw new HouseException(Message.HOUSE_ADDRESS_ID_NOT_MATCH);
            }
            for(HouseFacility facility: facilities){
                Optional<Facility> existFacility = facilityRepository.findById(facility.getId());
                if(existFacility.isEmpty()|| existFacility.get().getHouse() != id){
                    throw new HouseException(Message.HOUSE_FACILITY_ID_NOT_MATCH);
                }
            }


            int updatedRoom = 0;
            if(houseUpdateRequest.getStatus().equals("Inactive")){
                List<Room> rooms = roomRepository.findRoomsByHouseId(id);

                for(Room room: rooms){
                    updatedRoom = roomRepository.updateStatusById(room.getId(), "Inactive");
                }
            }
            if(houseUpdateRequest.getStatus().equals("Active")){
                List<Room> rooms = roomRepository.findRoomsByHouseId(id);

                for(Room room: rooms){
                    updatedRoom = roomRepository.updateStatusById(room.getId(), "Active");
                }
            }
            int updatedHouseRow = houseRepository.updateHouseById(id, houseUpdateRequest.getName(),
                    houseUpdateRequest.getAddress(), houseUpdateRequest.getEstablishDate(), houseUpdateRequest.getManager(),
                    houseUpdateRequest.getStatus(), houseUpdateRequest.getDescription());
            Address address = houseUpdateRequest.getAddress();
            int updatedAddressRow = addressRepository.updateAddress(address.getId(), address.getCity(), address.getDistrict(), address.getWard(), address.getStreet(), address.getHouseNumber());
            int updatedFacilityRow = 0;
            for(HouseFacility facility: facilities){
                updatedFacilityRow = facilityRepository.updateFacilityById(facility.getId(), facility.getName(), facility.getUnit(), facility.getPrice());
            }
            if(updatedHouseRow > 0 && updatedAddressRow >0 && updatedFacilityRow >0) {
                houseUpdateRequest.setId(id);
                return houseUpdateRequest;
            }else{
                return null;
            }

    }

    @Override
    public Page<HouseResponse> searchHouseByKeywords(String keywords, int page, int size){
        if(keywords != null){
            List<House> houses = houseRepository.searchHouses(keywords.toLowerCase());
            Collections.sort(houses, Comparator.comparing(House::getCreatedAt).reversed());
            return getHouseResponses(page, size, houses);
        }
        List<House> houses = houseRepository.findAll();
        Collections.sort(houses, Comparator.comparing(House::getCreatedAt).reversed());
        return getHouseResponses(page, size, houses);
    }

    private Page<HouseResponse> getHouseResponses(int page, int size, List<House> houses) {
        List<HouseResponse> houseResponseList = new ArrayList<>();
        for(House house: houses){
            List<HouseFacility> facilitiesResponseList = new ArrayList<>();
            List<Facility> facilities = facilityRepository.findFacilitiesByHouse(house.getId());
            for(Facility facility: facilities){
                facilitiesResponseList.add(new HouseFacility(facility.getId(),facility.getName(),facility.getUnit(), facility.getPrice()));
            }
            List<Map<String, Object>> imageResponse = new ArrayList<>();
            List<Image> images = imageService.getImagesByEntityId(house.getId(), "house");
            for(Image image: images){
                Map<String, Object> imageData = new HashMap<>();
                imageData.put("id", image.getId());
                imageData.put("url", image.getUrl());
                imageResponse.add(imageData);
            }
//            String managerName = userRepository.findUserById(house.getManager()).getName();
            houseResponseList.add(HouseResponse.toHouseResponse(house, facilitiesResponseList, house.getManager(), imageResponse));
        }
        Pageable pageable = PageRequest.of(page, size);
        return getHouseResponse(pageable, houseResponseList);
    }
}
