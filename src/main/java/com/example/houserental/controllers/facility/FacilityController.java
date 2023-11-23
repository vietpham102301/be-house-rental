package com.example.houserental.controllers.facility;


import com.example.houserental.controllers.models.FacilityChargeRequest;
import com.example.houserental.services.facility.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
@Slf4j
public class FacilityController {
    private final FacilityService facilityService;
    @GetMapping("/list/{roomId}")
    public ResponseEntity<Object> listFacilities(@PathVariable int roomId){
        return ResponseEntity.ok(facilityService.listFacilities(roomId));
    }
    @PostMapping("/charge")
    public ResponseEntity<Object> listFacilityCharges(@RequestBody FacilityChargeRequest[] request){
        return ResponseEntity.ok(facilityService.listFacilityCharges(request));
    }
}
