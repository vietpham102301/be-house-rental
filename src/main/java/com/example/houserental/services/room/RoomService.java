package com.example.houserental.services.room;

import com.example.houserental.controllers.models.RoomCreateRequest;
import com.example.houserental.controllers.models.RoomResponse;
import com.example.houserental.controllers.models.RoomUpdateRequest;
import org.springframework.data.domain.Page;

public interface RoomService {
    RoomResponse createRoom(RoomCreateRequest roomCreateRequest);
    boolean removeRoomById(int id);
    boolean updateRoomById(int id, RoomUpdateRequest roomUpdateRequest);

    Page<RoomResponse> listRoomsFilter(Integer page, Integer size, String houseName, Integer floor,String status);
    Page<RoomResponse> searchRoomsByKeywords(String keywords, Integer page, Integer size);
}
