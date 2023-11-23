package com.example.houserental.services.room;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.InvoiceResponse;
import com.example.houserental.controllers.models.RoomCreateRequest;
import com.example.houserental.controllers.models.RoomResponse;
import com.example.houserental.controllers.models.RoomUpdateRequest;
import com.example.houserental.exception.house.HouseException;
import com.example.houserental.exception.room.RoomException;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.repositories.house.HouseRepository;
import com.example.houserental.internal.repositories.room.RoomRepository;
import com.example.houserental.internal.repositories.room.ServiceRepository;
import com.example.houserental.services.image.ImageService;
import com.example.houserental.services.room.helper.RoomSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final HouseRepository houseRepository;
    private final ServiceRepository serviceRepository;
    private final ImageService imageService;
    @Override
    @Transactional
    public RoomResponse createRoom(RoomCreateRequest roomCreateRequest) {

        House house = houseRepository.findById(roomCreateRequest.getHouseId());
        if(house == null){
            throw new HouseException(Message.HOUSE_NOTFOUND);
        }
        if(house.getStatus().equals("Inactive")){
            throw new HouseException(Message.HOUSE_INACTIVE);
        }

        int insertedRoom = roomRepository.insertRoom(roomCreateRequest.getName(), roomCreateRequest.getHouseId(),
                roomCreateRequest.getFloor(), roomCreateRequest.getArea(),
                roomCreateRequest.getStatus(), roomCreateRequest.getRentFee(),
                roomCreateRequest.getDescription(), roomCreateRequest.getCapacity(), 0, new Date());

        int roomId = roomRepository.getLastInsertedId();
        for(com.example.houserental.controllers.models.Service service: roomCreateRequest.getServices()){
            serviceRepository.save(new com.example.houserental.internal.models.room.Service(service.getName(), roomId));
        }
        int totalRoom = house.getTotalRoom() + 1;

        int updatedHouse = houseRepository.updateHouseTotalRoomById(house.getId(), totalRoom);

        Optional<Room> room = roomRepository.findById(roomId);
        if(insertedRoom >0 && updatedHouse > 0){
            return RoomResponse.toRoomResponse(room.get(), new ArrayList<>(), new ArrayList<>());
        }
        return null;
    }

    @Override
    @Transactional
    public boolean removeRoomById(int id) {

        Optional<Room> room = roomRepository.findById(id);
        if(!room.isPresent()){
            throw new RoomException(Message.ROOM_NOTFOUND);
        }
        if(room.get().getStatus().equals("Inactive")){
            throw new RoomException(Message.ROOM_INACTIVE);
        }

        int updatedRoom = roomRepository.updateStatusById(id, "Inactive");
        int updatedHouse = houseRepository.updateHouseTotalRoomById(room.get().getHouse().getId(), room.get().getHouse().getTotalRoom() - 1);
        if(updatedRoom > 0 && updatedHouse > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateRoomById(int id, RoomUpdateRequest roomUpdateRequest) {
        Optional<Room> room = roomRepository.findById(id);
        if(!room.isPresent()){
            throw new RoomException(Message.ROOM_NOTFOUND);
        }
        int updatedRoom = roomRepository.updateRoomById(id, roomUpdateRequest.getName(), roomUpdateRequest.getFloor(), roomUpdateRequest.getArea(), roomUpdateRequest.getStatus(), roomUpdateRequest.getRentFee(), roomUpdateRequest.getDescription(), roomUpdateRequest.getCapacity());

        if(room.get().getStatus().equals("Active") && roomUpdateRequest.getStatus().equals("Inactive")){
            houseRepository.updateHouseTotalRoomById(room.get().getHouse().getId(), room.get().getHouse().getTotalRoom() - 1);
        }else if(room.get().getStatus().equals("Inactive") && roomUpdateRequest.getStatus().equals("Active")){
            houseRepository.updateHouseTotalRoomById(room.get().getHouse().getId(), room.get().getHouse().getTotalRoom() + 1);
        }
        if(updatedRoom > 0){
            return true;
        }
        return false;
    }

    @Override
    public Page<RoomResponse> listRoomsFilter(Integer page, Integer size, String houseName, Integer floor,String status) {
        List<RoomResponse> roomResponseList = new ArrayList<>();
        List<Room> rooms= roomRepository.findAll(RoomSpecification.withFilter(houseName, floor, status));
        return getRoomResponses(page, size, roomResponseList, rooms);
    }

    @Override
    public Page<RoomResponse> searchRoomsByKeywords(String keywords, Integer page, Integer size) {
        List<RoomResponse> roomResponseList = new ArrayList<>();
        List<Room> rooms= roomRepository.findAll(RoomSpecification.withSearchKeywords(keywords));
        Collections.sort(rooms, Collections.reverseOrder(Comparator.comparing(Room::getCreatedAt)));
        return getRoomResponses(page, size, roomResponseList, rooms);
    }

    private Page<RoomResponse> getRoomResponses(Integer page, Integer size, List<RoomResponse> roomResponseList, List<Room> rooms) {
        for(Room room: rooms){
            List<com.example.houserental.internal.models.room.Service> services = serviceRepository.findByRoom(room.getId());
            if(services == null){
                services = new ArrayList<>();
            }
            List<Image> images = imageService.getImagesByEntityId(room.getId(), "room");
            List<Map<String, Object>> imageResponse = new ArrayList<>();
            for(Image image: images){
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageResponse.add(imageMap);
            }
            roomResponseList.add(RoomResponse.toRoomResponse(room, services, imageResponse));
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<RoomResponse> response = getRoomResponse(pageable, roomResponseList);
        return response;
    }


    private Page<RoomResponse> getRoomResponse(Pageable pageable, List<RoomResponse> resultFilter) {

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), resultFilter.size());
        final Page<RoomResponse> response = new PageImpl<>(resultFilter.subList(start, end), pageable, resultFilter.size());
        return response;
    }



}
