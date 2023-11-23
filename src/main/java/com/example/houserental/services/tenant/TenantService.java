package com.example.houserental.services.tenant;


import com.example.houserental.controllers.models.TenantInforResponse;
import com.example.houserental.controllers.models.TenantUpdateRequest;
import com.example.houserental.internal.models.tenant.Tenant;
import org.springframework.data.domain.Page;



public interface TenantService {
    Tenant createTenant(Tenant tenant, int roomId);
    Page<TenantInforResponse> listFilter(int page, int size, String house, String room, String status);

    boolean removeTenantById(int id);
    boolean updateTenantByTenantRoomId(int id, TenantUpdateRequest tenantUpdateRequest);

    Page<TenantInforResponse> searchTenantsByKeywords(String keywords, int page, int size);

}
