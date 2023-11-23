package com.example.houserental.services.statistics;

import com.example.houserental.controllers.models.EntityGrow;
import com.example.houserental.controllers.models.GeneralGrow;
import com.example.houserental.internal.models.invoice.InvoiceDetail;
import com.example.houserental.internal.repositories.facility.FacilityHistoryRepository;
import com.example.houserental.internal.repositories.invoice.InvoiceDetailsRepository;
import com.example.houserental.internal.repositories.tenantsrooms.TenantsRoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticService{
    private final InvoiceDetailsRepository invoiceDetailRepository;
    private final TenantsRoomsRepository tenantsRoomsRepository;
    private final FacilityHistoryRepository facilityHistoryRepository;
    @Override
    public GeneralGrow generalGrowStatisticFromLastMonth() {
        GeneralGrow generalGrow = new GeneralGrow();
        List<InvoiceDetail> invoiceDetailsCurrentMonth = invoiceDetailRepository.findAllByCreatedAtBetween(getFirstDateOfCurrentMonth(), getLastDateOfCurrentMonth());
        List<InvoiceDetail> invoiceDetailsPreviousMonth = invoiceDetailRepository.findAllByCreatedAtBetween(getFirstDateOfPreviousMonth(), getLastDateOfPreviousMonth());
        Float revenueCurrentMonth = 0.0f;
        Float revenuePreviousMonth = 0.0f;
        if (invoiceDetailsCurrentMonth != null) {
            for (InvoiceDetail invoiceDetail : invoiceDetailsCurrentMonth) {
                Float facilityFee = facilityHistoryRepository.sumPriceByInvoiceDetailsAndCreatedAtBetween(invoiceDetail.getId(), getFirstDateOfCurrentMonth(), getLastDateOfCurrentMonth());
                revenueCurrentMonth += invoiceDetail.getRentFee() + facilityFee;
            }
        }
        if(invoiceDetailsPreviousMonth != null){
            for(InvoiceDetail invoiceDetail : invoiceDetailsPreviousMonth){
                Float facilityFee = facilityHistoryRepository.sumPriceByInvoiceDetailsAndCreatedAtBetween(invoiceDetail.getId(), getFirstDateOfPreviousMonth(), getLastDateOfPreviousMonth());
                revenuePreviousMonth += invoiceDetail.getRentFee() + facilityFee;
            }
        }
        Float revenueGrow;
        if(revenuePreviousMonth == 0){
            revenueGrow = 100.0f;
        }else{
            revenueGrow = ((revenueCurrentMonth - revenuePreviousMonth)/revenuePreviousMonth)*100;
        }
        EntityGrow revenue = new EntityGrow("Revenue", revenueCurrentMonth, revenueGrow);

        Integer tenantsCurrentMonth = tenantsRoomsRepository.countByRentDateBetween(getFirstDateOfCurrentMonth(), getLastDateOfCurrentMonth());
        Integer tenantsPreviousMonth = tenantsRoomsRepository.countByRentDateBetween(getFirstDateOfPreviousMonth(), getLastDateOfPreviousMonth());
        if(tenantsCurrentMonth == null){
            tenantsCurrentMonth = 0;
        }
        if(tenantsPreviousMonth == null){
            tenantsPreviousMonth = 0;
        }
        Double tenantsGrow;
        if(tenantsPreviousMonth == 0){
            tenantsGrow = 100.0;
        }else{
            tenantsGrow = ((tenantsCurrentMonth - tenantsPreviousMonth)/tenantsPreviousMonth)*100.0;
        }
        EntityGrow tenants = new EntityGrow("Tenants", tenantsCurrentMonth, tenantsGrow);

        Integer roomsCurrentMonth = tenantsRoomsRepository.countDistinctRoomIdByRentDateBetween(getFirstDateOfCurrentMonth(), getLastDateOfCurrentMonth());
        Integer roomsPreviousMonth = tenantsRoomsRepository.countDistinctRoomIdByRentDateBetween(getFirstDateOfPreviousMonth(), getLastDateOfPreviousMonth());
        if(roomsCurrentMonth == null){
            roomsCurrentMonth = 0;
        }
        if(roomsPreviousMonth == null){
            roomsPreviousMonth = 0;
        }
        Double roomsGrow;
        if(roomsPreviousMonth == 0){
            roomsGrow = 100.0;
        }else{
            roomsGrow= ((roomsCurrentMonth - roomsPreviousMonth)/roomsPreviousMonth)*100.0;
        }
        EntityGrow rooms = new EntityGrow("Rooms", roomsCurrentMonth, roomsGrow);

        generalGrow.setData(new EntityGrow[]{revenue, tenants, rooms});
        return generalGrow;
    }

    public static Date getFirstDateOfCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDate = currentDate.withDayOfMonth(1);
        return convertLocalDateToDate(firstDate);
    }

    public static Date getLastDateOfCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int lastDayOfMonth = currentDate.getMonth().length(currentDate.isLeapYear());
        LocalDate lastDate = currentDate.withDayOfMonth(lastDayOfMonth);
        return convertLocalDateToDate(lastDate);
    }

    public static Date getFirstDateOfPreviousMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDateOfCurrentMonth = currentDate.withDayOfMonth(1);
        LocalDate firstDateOfPreviousMonth = firstDateOfCurrentMonth.minusMonths(1);
        return convertLocalDateToDate(firstDateOfPreviousMonth);
    }

    public static Date getLastDateOfPreviousMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDateOfCurrentMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDateOfPreviousMonth = firstDateOfCurrentMonth.minusDays(1);
        return convertLocalDateToDate(lastDateOfPreviousMonth);
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
