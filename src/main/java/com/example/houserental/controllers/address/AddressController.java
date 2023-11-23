package com.example.houserental.controllers.address;


import com.example.houserental.services.address.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/cities")
    public ResponseEntity<Object> getAllCities(){
        Map<String, String[]> res = new HashMap<>();

        String[] cities = addressService.getAllCities();
        if(cities != null){
            res.put("cities", cities);
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/districts")
    public ResponseEntity<Object> getAllDistricts(@RequestParam(value ="city") String city){
        Map<String, String[]> res = new HashMap<>();

        String[] districts = addressService.getAllDistricts(city);
        if(districts != null){
            res.put("districts", districts);
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.internalServerError().build();
    }


    @GetMapping("/wards")
    public ResponseEntity<Object> getAllWards(@RequestParam(value ="city") String city, @RequestParam(value ="district") String district){
        Map<String, String[]> res = new HashMap<>();

        String[] wards = addressService.getAllWards(city, district);
        if(wards != null){
            res.put("wards", wards);
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.internalServerError().build();
    }



}
