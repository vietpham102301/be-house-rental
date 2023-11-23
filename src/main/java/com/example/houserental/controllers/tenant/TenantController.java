package com.example.houserental.controllers.tenant;


import com.example.houserental.controllers.helper.ListCommonRes;
import com.example.houserental.controllers.models.TenantCreateRequest;
import com.example.houserental.controllers.models.TenantCreateResponse;
import com.example.houserental.controllers.models.TenantInforResponse;
import com.example.houserental.controllers.models.TenantUpdateRequest;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import com.example.houserental.services.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenants")
@Slf4j
public class TenantController {
    private final TenantService tenantService;
    @PostMapping
    public ResponseEntity<Object> createTenant(@RequestBody TenantCreateRequest request){
        TenantCreateResponse res = TenantCreateResponse.toResponse(tenantService.createTenant(Tenant.convertToTenant(request), request.getRoomId()));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listFiler(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String houseName,
                                            @RequestParam(required = false) String roomName,
                                            @RequestParam(required = false) String status){
        Page<TenantInforResponse> filteredTenants = tenantService.listFilter(page, size, houseName, roomName, status);
        if(filteredTenants != null){
            Map<String, Object> res = ListCommonRes.toResponse(filteredTenants);
            return new ResponseEntity<>(res, null, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeTenantById(@PathVariable int id){
        if(tenantService.removeTenantById(id)){
            Map<String, String> res = new HashMap<>();
            res.put("message", "Tenant has been removed");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTenantById(@PathVariable int id, @RequestBody TenantUpdateRequest request){
        if(tenantService.updateTenantByTenantRoomId(id, request)){
            Map<String, String> res = new HashMap<>();
            res.put("message", "Tenant has been updated");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchTenantsByKeywords(@RequestParam(required = false) String keywords, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<TenantInforResponse> tenants = tenantService.searchTenantsByKeywords(keywords, page, size);
        if(tenants != null){
            Map<String, Object> res = ListCommonRes.toResponse(tenants);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

