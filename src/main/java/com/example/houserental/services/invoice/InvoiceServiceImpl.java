package com.example.houserental.services.invoice;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.*;
import com.example.houserental.exception.facility.FacilityException;
import com.example.houserental.exception.invoice.InvoiceException;
import com.example.houserental.exception.room.RoomException;
import com.example.houserental.exception.tenant.TenantException;
import com.example.houserental.internal.models.facility.FacilityHistory;
import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.models.invoice.Invoice;
import com.example.houserental.internal.models.invoice.InvoiceDetail;
import com.example.houserental.internal.models.room.Room;
import com.example.houserental.internal.models.tenant.Tenant;
import com.example.houserental.internal.models.tenant.TenantsRooms;
import com.example.houserental.internal.repositories.facility.FacilityHistoryRepository;
import com.example.houserental.internal.repositories.facility.FacilityRepository;
import com.example.houserental.internal.repositories.invoice.InvoiceDetailsRepository;
import com.example.houserental.internal.repositories.invoice.InvoiceRepository;
import com.example.houserental.internal.repositories.room.RoomRepository;
import com.example.houserental.internal.repositories.tenant.TenantRepository;
import com.example.houserental.internal.repositories.tenantsrooms.TenantsRoomsRepository;
import com.example.houserental.services.image.ImageService;
import com.example.houserental.services.invoice.helper.InvoiceSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailsRepository invoiceDetailRepository;
    private final RoomRepository roomRepository;
    private final FacilityHistoryRepository facilityHistoryRepository;
    private final TenantRepository tenantRepository;
    private final FacilityRepository facilityRepository;
    private final TenantsRoomsRepository tenantsRoomsRepository;
    private final ImageService imageService;


    @Override
    @Transactional
    public boolean createInvoice(InvoiceCreateRequest invoiceCreateRequest) {

        Tenant tenant = tenantRepository.findTenantById(invoiceCreateRequest.getTenantId());
        if(tenant == null){
            throw new TenantException(Message.TENANT_NOTFOUND);
        }
        List<TenantsRooms> tenantsRooms = tenantsRoomsRepository.findTenantsRoomsByTenantId(invoiceCreateRequest.getTenantId());
        if(tenantsRooms.isEmpty()){
            throw new TenantException(Message.TENANT_NOT_RENT_ROOM);
        }
        int count = 0;
        for(TenantsRooms t: tenantsRooms){
            if(t.getRoom().getId() == invoiceCreateRequest.getRoomId()){
                break;
            }else if(count == tenantsRooms.size() - 1){
                throw new TenantException(Message.TENANT_NOT_RENT_ROOM);
            }
            count++;
        }
        if(tenantsRooms.get(count).getStatus().equals("Inactive")){
            throw new TenantException(Message.TENANT_NOT_RENT_ROOM);
        }
        int insertedInvoice = invoiceRepository.insertInvoice(invoiceCreateRequest.getTenantId(),
                invoiceCreateRequest.getCreatorId(),
                invoiceCreateRequest.getPaymentMethod(), "Unpaid",
                invoiceCreateRequest.getClosingDate());
        Room room = roomRepository.findById(invoiceCreateRequest.getRoomId()).orElseThrow(()-> new RoomException(Message.ROOM_NOTFOUND));
        Integer invoiceId = invoiceRepository.getLastInsertedId();
        int insertedInvoiceDetails = invoiceDetailRepository.insertInvoiceDetails(invoiceId, invoiceCreateRequest.getRoomId(), room.getRentFee());
        FacilityHistory facilityHistory = null;
        Integer invoiceDetailId = invoiceDetailRepository.getLastInsertedId();

        //check enough facility
        List<com.example.houserental.internal.models.facility.Facility> facilities = facilityRepository.findFacilitiesByHouse(room.getHouse().getId());
        if(facilities.size() != invoiceCreateRequest.getFacilities().size()){
            throw new FacilityException(Message.NOT_EQUAL_QUANTITY);
        }

        for(Facility facility: invoiceCreateRequest.getFacilities()){
            com.example.houserental.internal.models.facility.Facility f = facilityRepository.findById(facility.getId()).orElseThrow(()-> new FacilityException(Message.FACILITY_NOTFOUND));
            if(f.getHouse() != room.getHouse().getId()){
                throw new FacilityException(Message.FACILITY_NOTFOUND);
            }
            Float pricePerUnit = f.getPrice();
            Float price;

            FacilityHistory existHistory = facilityHistoryRepository.findLatestByFacilityId(facility.getId(), room.getId());// neu luc dau khong co lich su thi se bi null
            if(existHistory == null){
                price = facility.getCurrentIndex() * pricePerUnit;
                facilityHistory = facilityHistoryRepository.save(new FacilityHistory(facility.getId(), invoiceCreateRequest.getRoomId(), facility.getCurrentIndex(), pricePerUnit, price, invoiceDetailId, 0));
                continue;
            }
            Integer previousIndex = existHistory.getUsageNumber();
            if(facility.getCurrentIndex() < previousIndex && !f.getUnit().equals("month")){
                throw new InvoiceException(Message.INVALID_INDEX);
            }else if(facility.getCurrentIndex() < 0){
                throw new InvoiceException(Message.INVALID_INDEX);
            }
            // check facility unit is month or not?
            if(f.getUnit().equals("month")){
                price = pricePerUnit*facility.getCurrentIndex(); // number of months * price per month
                facilityHistory = facilityHistoryRepository.save(new FacilityHistory(facility.getId(), invoiceCreateRequest.getRoomId(), facility.getCurrentIndex(), pricePerUnit, price, invoiceDetailId, previousIndex));
                continue;
            }
            price = (facility.getCurrentIndex() - previousIndex) * pricePerUnit;
            facilityHistory = facilityHistoryRepository.save(new FacilityHistory(facility.getId(), invoiceCreateRequest.getRoomId(),facility.getCurrentIndex(), pricePerUnit, price, invoiceDetailId, previousIndex));
        }

        if(insertedInvoiceDetails > 0 && facilityHistory != null && insertedInvoice >0){
            return true;
        }
        throw new InvoiceException(Message.UNKNOWN_ERR);
    }

    @Override
    public Page<InvoiceResponse> listInvoicesFilter(Integer page, Integer size, Date from, Integer houseId, String status) {
        List<InvoiceResponse> res = new ArrayList<>();
        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAll(InvoiceSpecification.withFilter(from, houseId, status));
        return toInvoiceResponses(page, size, res, invoiceDetails);
    }


    @Override
    public Page<InvoiceResponse> searchInvoicesByKeywords(String keywords, int page, int size) {
        List<InvoiceResponse> res = new ArrayList<>();
        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAll(InvoiceSpecification.withKeywords(keywords));
        return toInvoiceResponses(page, size, res, invoiceDetails);
    }

    @Override
    @Transactional
    public boolean updateInvoiceById(int id, UpdateInvoiceRequest updateRequest) {
        Invoice invoice = invoiceRepository.findById(id);
        if(invoice == null){
            throw new InvoiceException(Message.INVOICE_NOTFOUND);
        }
        //update invoices table

        int updatedInvoice = invoiceRepository.updateInvoiceById(id, updateRequest.getPaymentMethod(), updateRequest.getStatus(), updateRequest.getClosingDate());

        if(updatedInvoice > 0){
            InvoiceDetail invoiceDetail = invoiceDetailRepository.findByInvoiceId(id);

            //update facility_histories table
            for(Facility f: updateRequest.getFacilities()){
                FacilityHistory facilityHistoryOfInvoiceDetail = facilityHistoryRepository.findByInvoiceDetailsAndFacility(invoiceDetail.getId(), f.getId());
                if(facilityHistoryOfInvoiceDetail == null){
                    throw new InvoiceException(Message.FACILITY_DONT_EXIST); //if request update facility that not exist in facility_histories table
                }
                com.example.houserental.internal.models.facility.Facility facility = facilityRepository.findById(f.getId()).orElseThrow(()-> new FacilityException(Message.FACILITY_NOTFOUND));
                if(f.getCurrentIndex() < facilityHistoryOfInvoiceDetail.getPreviousIndex() && !facility.getUnit().equals("month")){
                    throw new InvoiceException(Message.INVALID_INDEX);
                }else if(f.getCurrentIndex() <= 0){
                    throw new InvoiceException(Message.INVALID_INDEX);
                }

                Float updatedPriceForThisInvoiceDetail;
                if(facility.getUnit().equals("month")){
                    updatedPriceForThisInvoiceDetail = f.getCurrentIndex() * facilityHistoryOfInvoiceDetail.getPricePerUnit();
                }else{
                    updatedPriceForThisInvoiceDetail = (f.getCurrentIndex() - facilityHistoryOfInvoiceDetail.getPreviousIndex()) * facilityHistoryOfInvoiceDetail.getPricePerUnit();;
                }

               int updatedFH = facilityHistoryRepository.updateFacilityUsageNumberByInvoiceDetailsAndFacility(invoiceDetail.getId(), f.getId(), f.getCurrentIndex(), updatedPriceForThisInvoiceDetail);
                if(updatedFH == 0){
                     throw new InvoiceException(Message.UNKNOWN_ERR);
                }

               FacilityHistory facilityHistoryNearestUpdatedOne = facilityHistoryRepository.findNearestRecord(facilityHistoryOfInvoiceDetail.getCreatedAt(), f.getId()); //find nearest record of facility_history

               if(facilityHistoryNearestUpdatedOne != null) {
                   if(facilityHistoryNearestUpdatedOne.getUsageNumber() < f.getCurrentIndex() && !facility.getUnit().equals("month")){
                       throw new InvoiceException(Message.NEXT_INDEX_MUST_LARGER);
                   }
                   Float updatedPriceForNextInvoiceDetail;
                   if(facility.getUnit().equals("month")){
                       updatedPriceForNextInvoiceDetail = facilityHistoryNearestUpdatedOne.getUsageNumber() * facilityHistoryNearestUpdatedOne.getPricePerUnit();
                   }else{
                       updatedPriceForNextInvoiceDetail = (facilityHistoryNearestUpdatedOne.getUsageNumber() - f.getCurrentIndex()) * facilityHistoryNearestUpdatedOne.getPricePerUnit();
                   }
                   int updatedPreIndex = facilityHistoryRepository.updateFacilityHistoriesPreviousIndexById(facilityHistoryNearestUpdatedOne.getId(), f.getCurrentIndex(), updatedPriceForNextInvoiceDetail);
                     if(updatedPreIndex == 0){
                          throw new InvoiceException(Message.UNKNOWN_ERR);
                     }
               }

            }
            return true;
        }else{
            throw new InvoiceException(Message.UNKNOWN_ERR);
        }
    }

    private Page<InvoiceResponse> toInvoiceResponses(int page, int size, List<InvoiceResponse> res, List<InvoiceDetail> invoiceDetails) {
        for(InvoiceDetail invoiceDetail: invoiceDetails){
            Integer invoiceId = invoiceDetail.getInvoice().getId();
            String tenantName = invoiceDetail.getInvoice().getTenant().getName();
            String roomName = invoiceDetail.getRoom().getName();
            String houseName = invoiceDetail.getRoom().getHouse().getName();
            String creatorName = invoiceDetail.getInvoice().getCreator().getName();
            Date createdAt = invoiceDetail.getInvoice().getCreatedAt();
            String paymentMethod = invoiceDetail.getInvoice().getPaymentMethod();
            Date closingDate = invoiceDetail.getInvoice().getClosingDate();
            String statusInvoice = invoiceDetail.getInvoice().getStatus();
            String tenantEmail = invoiceDetail.getInvoice().getTenant().getEmail();

            List<Map<String, Object>> imageResponse = new ArrayList<>();
            for(Image image: imageService.getImagesByEntityId(invoiceDetail.getInvoice().getTenant().getId(), "tenant")){
                Map<String, Object> imageDataMap = new HashMap<>();
                imageDataMap.put("id", image.getId());
                imageDataMap.put("url", image.getUrl());
                imageResponse.add(imageDataMap);
            }


            List<FacilityHistory> facilityHistories = facilityHistoryRepository.findByInvoiceDetails(invoiceDetail.getId());
            com.example.houserental.controllers.models.InvoiceDetail detailsRes = new com.example.houserental.controllers.models.InvoiceDetail();
            List<com.example.houserental.controllers.models.FacilityHistory> facilityHistoriesResponse = new ArrayList<>();

            Float totalPrice = 0f;
            for(FacilityHistory facilityHistory:  facilityHistories){
                com.example.houserental.internal.models.facility.Facility facility = facilityRepository.findById(facilityHistory.getFacility()).orElseThrow(()-> new InvoiceException(Message.FACILITY_NOTFOUND));
                Integer previousIndex = facilityHistory.getPreviousIndex();
                Integer currentIndex = facilityHistory.getUsageNumber();
                Integer usage = currentIndex - previousIndex;
                if(facility.getUnit().equals("month")){
                    usage = currentIndex;
                }

                Float pricePerUnit = facilityHistory.getPricePerUnit();
                Float price = facilityHistory.getPrice();
                totalPrice += price;
                com.example.houserental.controllers.models.FacilityHistory f = new com.example.houserental.controllers.models.FacilityHistory(facilityHistory.getFacility(),facility.getName(), previousIndex, currentIndex, usage, pricePerUnit, facility.getUnit(), price);
                facilityHistoriesResponse.add(f);
            }
            //total price for facilities
            detailsRes.setTotalFacilitiesPrice(totalPrice);

            // for rents
            facilityHistoriesResponse.add(new com.example.houserental.controllers.models.FacilityHistory(0,"Rents", 0, 0, 0, invoiceDetail.getRentFee(), "month",invoiceDetail.getRentFee()));

            detailsRes.setFacilityHistories(facilityHistoriesResponse);

            totalPrice += invoiceDetail.getRentFee();


            res.add(new InvoiceResponse(invoiceId, tenantName, tenantEmail, roomName, houseName, creatorName, paymentMethod, closingDate, statusInvoice, detailsRes, totalPrice, createdAt, imageResponse));
            ;
        }

        return getInvoiceResponse(PageRequest.of(page, size), res);
    }

    @Override
    @Transactional
    public boolean cancelInvoiceById(int id) {
        Invoice invoice = invoiceRepository.findById(id);
       if(invoice == null){
           throw new InvoiceException(Message.INVOICE_NOTFOUND);
       }
         if(invoice.getStatus().equals("Cancel")){
              throw new InvoiceException(Message.INVOICE_CANCEL);
         }
       int updatedRow = invoiceRepository.updateInvoiceStatusById(id, "Cancel");

       return updatedRow > 0;
    }

//    @Override
//    @Transactional
//    public boolean deleteInvoiceById(int id){
//        InvoiceDetail invoiceDetail = invoiceDetailRepository.findByInvoiceId(id);
//        if(invoiceDetail == null){
//            throw new InvoiceException(Message.INVOICE_NOTFOUND);
//        }
//
//        int deletedFacilityHistoryRow = facilityHistoryRepository.deleteByInvoiceDetailsId(invoiceDetail.getId());
//
//        int deletedInvoiceDetailRow = invoiceDetailRepository.deleteByInvoiceId(id);
//
//        int deletedInvoiceRow = invoiceRepository.deleteInvoiceById(id);
//
//        return deletedFacilityHistoryRow > 0 && deletedInvoiceDetailRow > 0 && deletedInvoiceRow > 0;
//    }




    private Page<InvoiceResponse> getInvoiceResponse(Pageable pageable, List<InvoiceResponse> resultFilter) {

        Collections.sort(resultFilter, (invoice1, invoice2) -> invoice2.getCreatedAt().compareTo(invoice1.getCreatedAt()));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), resultFilter.size());
        final Page<InvoiceResponse> response = new PageImpl<>(resultFilter.subList(start, end), pageable, resultFilter.size());

        return response;
    }



    @Override
    public Page<InvoiceRoomResponse> listInvoiceByRoomId(int id, int page, int size){

        Room existRoom = roomRepository.findRoomById(id);
        if(existRoom == null){
            throw new RoomException(Message.ROOM_NOTFOUND);
        }

        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAll(InvoiceSpecification.withRoomId(id));
        List<InvoiceRoomResponse> res = new ArrayList<>();

        for(InvoiceDetail invoiceDetail: invoiceDetails){
            int invoiceID = invoiceDetail.getInvoice().getId();
            Date createdAt = invoiceDetail.getInvoice().getCreatedAt();
            String status = invoiceDetail.getInvoice().getStatus();
            Float total = 0f;

            List<FacilityHistory> facilityHistories = facilityHistoryRepository.findByInvoiceDetails(invoiceDetail.getId());

            for(FacilityHistory facilityHistory: facilityHistories){
                total += facilityHistory.getPrice();
            }
            total += invoiceDetail.getRentFee();

            res.add(new InvoiceRoomResponse(invoiceID, createdAt, total, status));
        }

        return getInvoiceRoomResponse(PageRequest.of(page, size), res);
    }

    private Page<InvoiceRoomResponse> getInvoiceRoomResponse(Pageable pageable, List<InvoiceRoomResponse> resultFilter) {
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), resultFilter.size());
        final Page<InvoiceRoomResponse> response = new PageImpl<>(resultFilter.subList(start, end), pageable, resultFilter.size());
        return response;
    }

}
