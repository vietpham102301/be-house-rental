package com.example.houserental.services.tenant;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.TenantInforResponse;
import com.example.houserental.controllers.models.TenantUpdateRequest;
import com.example.houserental.exception.tenant.TenantException;
import com.example.houserental.internal.models.address.Address;
import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import com.example.houserental.internal.repositories.address.AddressRepository;
import com.example.houserental.internal.repositories.room.RoomRepository;
import com.example.houserental.internal.repositories.tenant.TenantRepository;
import com.example.houserental.internal.repositories.tenantsrooms.TenantsRoomsRepository;
import com.example.houserental.services.image.ImageService;
import com.example.houserental.services.tenantsrooms.TenantsRoomsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService{

    private final TenantRepository tenantRepository;
    private final AddressRepository addressRepository;
    private final TenantsRoomsService tenantsRoomsService;
    private final TenantsRoomsRepository tenantsRoomsRepository;
    private final ImageService imageService;
    private final RoomRepository roomRepository;
    @Override
    @Transactional
    public Tenant createTenant(Tenant tenant, int roomId) {
        Address tenantAddress = tenant.getPermanentAddress();
        Address existAddress = addressRepository.findAddressByCityAndDistrictAndWardAndStreetAndHouseNumber(tenantAddress.getCity(), tenantAddress.getDistrict(), tenantAddress.getWard(), tenantAddress.getStreet(), tenantAddress.getHouseNumber());
        List<Tenant> existTenants =tenantRepository.findTenantByPhoneOrEmailOrIdNumberOrLicensePlate(tenant.getPhone(), tenant.getEmail(), tenant.getIdNumber(), tenant.getLicensePlate());
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent() &&room.get().getStatus().equals("Active")){
            int currentRentedTenant = tenantsRoomsRepository.countByRoomId(room.get().getId());
            if(currentRentedTenant < room.get().getCapacity()){
                if(existAddress == null && existTenants.size() == 0){
                    Address savedAddress = addressRepository.save(tenantAddress);
                    tenant.setPermanentAddress(savedAddress);
                    tenantRepository.save(tenant);
                    int insertedRow = tenantsRoomsRepository.insertTenantsRooms(tenant.getId(), roomId, "Active");
                    int currentTenant = room.get().getCurrentTenant() +1;
                    int updatedRoom = roomRepository.updateCurrentTenantById(roomId, currentTenant);
                    if(insertedRow > 0 && updatedRoom > 0){
                        return tenant;
                    }
                }else{
                    if(existAddress != null){
                        throw new TenantException(Message.Exist_ADDRESS);
                    }else if(existTenants != null){
                        for(Tenant t: existTenants){
                            if(t.getPhone().equals(tenant.getPhone())){
                                throw new TenantException(Message.PHONE_UNIQUE);
                            }else if(t.getEmail().equals(tenant.getEmail())){
                                throw new TenantException(Message.EMAIL_UNIQUE);
                            }else if(t.getIdNumber().equals(tenant.getIdNumber())){
                                throw new TenantException(Message.ID_NUM_UNIQUE);
                            }else if(t.getLicensePlate().equals(tenant.getLicensePlate())){
                                throw new TenantException(Message.EXIST_LICENSE_PLATE);
                            }
                        }
                    }
                }
                throw new TenantException(Message.UNKNOWN_ERR);
            }else{
                throw new TenantException(Message.ROOM_FULL);
            }
        }else if(room.isPresent() && room.get().getStatus().equals("Inactive")){
            throw new TenantException(Message.ROOM_INACTIVE);
        }else {
            throw new TenantException(Message.ROOM_NOTFOUND);
        }

    }

    @Override
    public Page<TenantInforResponse> listFilter(int pageNumber, int pageSize, String houseName, String roomName, String status) {
        Page<TenantsRooms> filteredTenants = tenantsRoomsService.listFilter(pageNumber, pageSize, houseName, roomName, status);
        Page<TenantInforResponse> res = filteredTenants.map(tenantsRooms -> TenantInforResponse.toResponse(tenantsRooms));
        for(TenantInforResponse tenantInforResponse: res){
            List<Image> images = imageService.getImagesByEntityId(tenantInforResponse.getId(), "tenant");
            List<Map<String, Object>> imageData = new ArrayList<>();
            for(Image image: images){
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageData.add(imageMap);
            }
            tenantInforResponse.setImageData(imageData);
        }
        return res;
    }

    @Override
    @Transactional
    public boolean removeTenantById(int id) {
        TenantsRooms tenantsRooms = tenantsRoomsRepository.findById(id);
        if(tenantsRooms != null){
            if(!tenantsRooms.getStatus().equals("Inactive")){
                int updatedRow = tenantsRoomsRepository.updateTenantsRoomsStatusById(id);
                int currentTenant = tenantsRooms.getRoom().getCurrentTenant() -1;
                int updatedRoom = roomRepository.updateCurrentTenantById(tenantsRooms.getRoom().getId(), currentTenant);
                return updatedRow > 0 && updatedRoom > 0;
            }else{
                throw new TenantException(Message.TENANT_INACTIVE);
            }
        }else{
            throw new TenantException(Message.TENANT_NOTFOUND);
        }
    }


    //todo: constraint unique for phone, email, idNumber, licensePlate, permanent address
    @Override
    @Transactional
    public boolean updateTenantByTenantRoomId(int tenantRoomId, TenantUpdateRequest tenantUpdateRequest) {

        TenantsRooms tenantsRooms = tenantsRoomsRepository.findById(tenantRoomId);
        if(tenantsRooms != null) {
            Tenant tenant = tenantRepository.findTenantById(tenantsRooms.getTenant().getId());
            int updatedTenantRoom = tenantsRoomsRepository.updateTenantsRoomsById(tenantRoomId,
                                                                            tenant.getId(), tenantUpdateRequest.getRoomID(),
                                                                            tenantUpdateRequest.getStatus());
            if(tenantUpdateRequest.getStatus().equals("Inactive") && tenantsRooms.getStatus().equals("Active")){
                int currentTenant = tenantsRooms.getRoom().getCurrentTenant() -1;
                int updatedRoom = roomRepository.updateCurrentTenantById(tenantUpdateRequest.getRoomID(), currentTenant);
                if(updatedRoom <= 0){
                    throw new TenantException(Message.UNKNOWN_ERR);
                }

            }else if(tenantUpdateRequest.getStatus().equals("Active") && tenantsRooms.getStatus().equals("Inactive")) {
                int currentTenant = tenantsRooms.getRoom().getCurrentTenant() +1;
                int updatedRoom = roomRepository.updateCurrentTenantById(tenantUpdateRequest.getRoomID(), currentTenant);
                if(updatedRoom <= 0){
                    throw new TenantException(Message.UNKNOWN_ERR);
                }
            }
            int updatedTenant = tenantRepository.updateTenantById(tenant.getId(), tenantUpdateRequest.getTenantName(),
                    tenantUpdateRequest.getBirthDate(), tenantUpdateRequest.getGender(), tenantUpdateRequest.getPhone(),
                    tenantUpdateRequest.getEmail(), tenantUpdateRequest.getIdNumber(),
                    tenantUpdateRequest.getLicensePlates(), tenantUpdateRequest.getRentDate(), tenantUpdateRequest.getDescription());
            int updatedAddress = addressRepository.updateAddress(tenantUpdateRequest.getPermanentAddress().getId(),
                    tenantUpdateRequest.getPermanentAddress().getCity(), tenantUpdateRequest.getPermanentAddress().getDistrict(),
                    tenantUpdateRequest.getPermanentAddress().getWard(), tenantUpdateRequest.getPermanentAddress().getStreet(),
                    tenantUpdateRequest.getPermanentAddress().getHouseNumber());
            if(updatedTenantRoom > 0 && updatedTenant > 0 && updatedAddress > 0){
                return true;
            }else {
                throw new TenantException(Message.UNKNOWN_ERR);
            }
        }else{
            throw new TenantException(Message.TENANT_NOTFOUND);
        }
    }



    @Override
    public Page<TenantInforResponse> searchTenantsByKeywords(String keywords, int pageNumber, int pageSize) {
        Page<TenantsRooms> searchedTenantByKeywords= tenantsRoomsService.search(keywords, pageNumber, pageSize);
        Page<TenantInforResponse> res = searchedTenantByKeywords.map(tenantsRooms -> TenantInforResponse.toResponse(tenantsRooms));
        for(TenantInforResponse tenantInforResponse: res){
            List<Image> images = imageService.getImagesByEntityId(tenantInforResponse.getId(), "tenant");
            List<Map<String, Object>> imageData = new ArrayList<>();
            for(Image image: images){
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageData.add(imageMap);
            }
            tenantInforResponse.setImageData(imageData);
        }

        return res;
    }
}
