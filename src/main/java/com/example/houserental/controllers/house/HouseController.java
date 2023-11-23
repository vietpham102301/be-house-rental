package com.example.houserental.controllers.house;
import com.example.houserental.controllers.helper.ListCommonRes;
import com.example.houserental.controllers.models.HouseCreateRequest;
import com.example.houserental.controllers.models.HouseCreateResponse;
import com.example.houserental.controllers.models.HouseResponse;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.services.house.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/houses")
@Slf4j
public class HouseController {
    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<?> createHouse(@Validated @RequestBody HouseCreateRequest houseCreateRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            Map<String, Object> res = new HashMap<>();
            res.put("errorMessage", errors);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        return houseService.createHouse(House.convertToHouse(houseCreateRequest), houseCreateRequest.getFacilities());
    }



    @GetMapping("list")
    public ResponseEntity<Object> listHouse(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String district,
                                                   @RequestParam(required = false) String ward,
                                                   @RequestParam(required = false) String status
                                            ){

        Page<HouseResponse> houses = houseService.listHouseWithFilter(page, size, city, district, ward, status);
        if(houses != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("houses", houses.getContent());
            response.put("currentPage", houses.getNumber());
            response.put("totalItems", houses.getTotalElements());
            response.put("totalPages", houses.getTotalPages());
            return new ResponseEntity<>(response, null, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeHouseById(@PathVariable("id") int id){
        HttpStatus status;
        boolean isSuccess = houseService.removeHouseById(id);
        if(isSuccess){
            status = HttpStatus.OK;
            Map<String, String> message = new HashMap<>();
            message.put("status", "success");
            message.put("message", "remove successful");
            return new ResponseEntity<>(message, null, status);
        }else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(null, null, status);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> modifyHouseById(@PathVariable("id") int id, @RequestBody @Validated HouseCreateRequest houseCreateRequest){
        HttpStatus status;
        House house = houseService.updateHouseById(id, House.convertToHouse(houseCreateRequest), houseCreateRequest.getFacilities());
        if(house != null){
            status = HttpStatus.OK;
            HouseCreateResponse res = HouseCreateResponse.toResponse(house);
            return new ResponseEntity<>(res, null, status);
        }else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(null, null, status);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchHouseByKeywords(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                        @RequestParam(required = false) String keywords){
        Page<HouseResponse> houses = houseService.searchHouseByKeywords(keywords, page, size);

        if(houses != null){
            return new ResponseEntity<>(ListCommonRes.toResponse(houses), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getHouseById(@PathVariable("id") int id){
        House house = houseService.getHouseById(id);
        if(house != null){
            return new ResponseEntity<>(house, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
