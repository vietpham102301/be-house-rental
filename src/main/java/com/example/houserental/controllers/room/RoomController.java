package com.example.houserental.controllers.room;


import com.example.houserental.controllers.helper.ListCommonRes;
import com.example.houserental.controllers.models.RoomCreateRequest;
import com.example.houserental.controllers.models.RoomResponse;
import com.example.houserental.controllers.models.RoomUpdateRequest;
import com.example.houserental.services.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@Slf4j
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Object> createRoom(@RequestBody RoomCreateRequest request){
        RoomResponse room = roomService.createRoom(request);
        if(room != null){
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeRoomById(@PathVariable("id") int id){
        if(roomService.removeRoomById(id)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Room has been Inactive");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRoomById(@PathVariable("id") int id, @RequestBody RoomUpdateRequest request){
        if(roomService.updateRoomById(id, request)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Room has been updated");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.internalServerError().build();

    }

    @GetMapping("/list")
    public ResponseEntity<Object> listFilter(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "houseName", required = false) String houseName,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "floor", required = false) Integer floor
                                            ){
        Page<RoomResponse> res = roomService.listRoomsFilter(page, size, houseName, floor, status);
        if(res != null){

            return ResponseEntity.ok(ListCommonRes.toResponse(res));
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(value = "keywords", required = false) String keywords,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        Page<RoomResponse> res = roomService.searchRoomsByKeywords(keywords, page, size);
        if(res != null){
            return ResponseEntity.ok(ListCommonRes.toResponse(res));
        }
        return ResponseEntity.internalServerError().build();
    }


}
