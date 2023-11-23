package com.example.houserental.services.invoice;

import com.example.houserental.controllers.models.InvoiceCreateRequest;
import com.example.houserental.controllers.models.InvoiceResponse;
import com.example.houserental.controllers.models.InvoiceRoomResponse;
import com.example.houserental.controllers.models.UpdateInvoiceRequest;
import org.springframework.data.domain.Page;

import java.util.Date;


public interface InvoiceService {
    boolean createInvoice(InvoiceCreateRequest invoiceCreateRequest);
    Page<InvoiceResponse> listInvoicesFilter(Integer page, Integer size, Date from, Integer houseId, String status);
    boolean cancelInvoiceById(int id); //cancel

    Page<InvoiceResponse> searchInvoicesByKeywords(String keywords, int page, int size);
    boolean updateInvoiceById(int id, UpdateInvoiceRequest request);

//    boolean deleteInvoiceById(int id);

    Page<InvoiceRoomResponse> listInvoiceByRoomId(int id, int page, int size);

}
