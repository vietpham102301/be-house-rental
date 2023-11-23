package com.example.houserental.services.tenantsrooms;

import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import com.example.houserental.internal.repositories.tenantsrooms.TenantsRoomsRepository;
import com.example.houserental.services.tenantsrooms.helper.TenantsRoomsSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantsRoomsImpl implements TenantsRoomsService{
    private final TenantsRoomsRepository tenantsRoomsRepository;
    @Override
    public Page<TenantsRooms> listFilter(int pageNumber, int pageSize, String houseName, String roomName, String status) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<TenantsRooms> res = tenantsRoomsRepository.findAll(TenantsRoomsSpecifications.withHouseRoomStatusFilters(houseName, roomName, status), page);

        return res;
    }

    @Override
    public Page<TenantsRooms> search(String keywords, int pageNumber, int pageSize) {
        Page<TenantsRooms> res = tenantsRoomsRepository.findAll(TenantsRoomsSpecifications.withKeywords(keywords), PageRequest.of(pageNumber, pageSize));
        return res;
    }

}
