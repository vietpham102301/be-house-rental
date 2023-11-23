package com.example.houserental.services.facility;


import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.FacilityCharge;
import com.example.houserental.controllers.models.FacilityChargeRequest;
import com.example.houserental.controllers.models.FacilityChargeResponse;
import com.example.houserental.controllers.models.FacilityResponse;
import com.example.houserental.exception.facility.FacilityException;
import com.example.houserental.exception.room.RoomException;
import com.example.houserental.internal.models.facility.Facility;
import com.example.houserental.internal.models.facility.FacilityHistory;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.repositories.facility.FacilityHistoryRepository;
import com.example.houserental.internal.repositories.facility.FacilityRepository;
import com.example.houserental.internal.repositories.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService{
    private final FacilityHistoryRepository facilityHistoryRepository;
    private final FacilityRepository facilityRepository;
    private final RoomRepository roomRepository;
    @Override
    public List<FacilityResponse> listFacilities(int roomId) {


        List<FacilityResponse> result = new ArrayList<>();
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomException(Message.ROOM_NOTFOUND));
        result.add(new FacilityResponse(0,"Rents", 0,room.getRentFee(), "month"));
        List<Facility> facilities = facilityRepository.findFacilitiesByHouse(room.getHouse().getId());
        if(facilities.isEmpty()){
            throw new FacilityException("This house has no facilities");
        }

        for(Facility facility: facilities){
            FacilityHistory facilityHistory = facilityHistoryRepository.findLatestByFacilityId(facility.getId(), roomId);
            if(facilityHistory != null){
                result.add(new FacilityResponse(facility.getId() ,facility.getName(), facilityHistory.getUsageNumber(), facility.getPrice(), facility.getUnit()));
            }else{
                result.add(new FacilityResponse(facility.getId(), facility.getName(), 0, facility.getPrice(), facility.getUnit()));
            }
        }

        return result;
    }

    @Override
    public FacilityChargeResponse listFacilityCharges(FacilityChargeRequest[] request) {
        List<FacilityCharge> result = new ArrayList<>();
        Float total = 0f;
        for(FacilityChargeRequest facilityChargeRequest: request){
            Facility facility = facilityRepository.findById(facilityChargeRequest.getId()).orElseThrow(()-> new FacilityException(Message.FACILITY_NOTFOUND));
            if(facility.getUnit().equals("month")){
                result.add(new FacilityCharge(facilityChargeRequest.getId(), facilityChargeRequest.getName(), facilityChargeRequest.getCurrentIndex(), 1, facilityChargeRequest.getUnitPrice()));
                total += facilityChargeRequest.getUnitPrice();
                continue;
            }
            Integer usageNumber = facilityChargeRequest.getCurrentIndex() - facilityChargeRequest.getPreviousIndex();
            Float totalPricePerFacility = facilityChargeRequest.getUnitPrice() * usageNumber;
            result.add(new FacilityCharge(facility.getId(), facilityChargeRequest.getName(), facilityChargeRequest.getCurrentIndex(), usageNumber,totalPricePerFacility));
            total += totalPricePerFacility;
        }


        return new FacilityChargeResponse(result, total);
    }
}
