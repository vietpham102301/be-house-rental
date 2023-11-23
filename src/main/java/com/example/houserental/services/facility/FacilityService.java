package com.example.houserental.services.facility;

import com.example.houserental.controllers.models.FacilityChargeRequest;
import com.example.houserental.controllers.models.FacilityChargeResponse;
import com.example.houserental.controllers.models.FacilityResponse;


import java.util.List;

public interface FacilityService {
    List<FacilityResponse> listFacilities(int roomId);
    FacilityChargeResponse listFacilityCharges(FacilityChargeRequest[] request);
}
