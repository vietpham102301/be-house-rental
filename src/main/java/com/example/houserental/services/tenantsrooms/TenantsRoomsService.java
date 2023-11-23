package com.example.houserental.services.tenantsrooms;

import com.example.houserental.internal.models.tenant.TenantsRooms;
import org.springframework.data.domain.Page;

public interface TenantsRoomsService {
    Page<TenantsRooms> listFilter(int pageNumber, int pageSize, String houseName, String roomName, String Status);
    Page<TenantsRooms> search(String keywords, int pageNumber, int pageSize);

}
