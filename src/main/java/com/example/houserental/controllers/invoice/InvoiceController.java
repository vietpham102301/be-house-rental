package com.example.houserental.controllers.invoice;


import com.example.houserental.controllers.helper.ListCommonRes;
import com.example.houserental.controllers.models.InvoiceCreateRequest;
import com.example.houserental.controllers.models.InvoiceResponse;
import com.example.houserental.controllers.models.InvoiceRoomResponse;
import com.example.houserental.controllers.models.UpdateInvoiceRequest;
import com.example.houserental.services.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
@Slf4j
public class InvoiceController {
    public final InvoiceService invoiceService;
    @PostMapping
    public ResponseEntity<Object> createInvoice(@RequestBody InvoiceCreateRequest request){
        if(invoiceService.createInvoice(request)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invoice has been created");
            response.put("status", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listInvoiceFilter(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                    @RequestParam(required = false) Integer houseId,
                                                    @RequestParam(required = false) String status){
        Page<InvoiceResponse> invoices = invoiceService.listInvoicesFilter(page, size, from, houseId, status);
        if(invoices != null){
            return new ResponseEntity<>(ListCommonRes.toResponse(invoices), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancelInvoiceById(@PathVariable int id){
        if(invoiceService.cancelInvoiceById(id)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invoice has been canceled");
            response.put("status", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchInvoice(@RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) String keywords){
        Page<InvoiceResponse> invoices = invoiceService.searchInvoicesByKeywords(keywords, page, size);
        if(invoices != null){
            return new ResponseEntity<>(ListCommonRes.toResponse(invoices), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @DeleteMapping("/permanent/{id}")
//    public ResponseEntity<Object> deleteInvoiceById(@PathVariable int id){
//        if(invoiceService.deleteInvoiceById(id)){
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Invoice has been deleted");
//            response.put("status", "OK");
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateInvoiceById(@PathVariable int id, @RequestBody UpdateInvoiceRequest request){
        if(invoiceService.updateInvoiceById(id, request)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invoice has been updated");
            response.put("status", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Object> listInvoiceByRoomId(@PathVariable int id,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size){
        Page<InvoiceRoomResponse> invoices = invoiceService.listInvoiceByRoomId(id, page, size);
        if(invoices != null){
            return new ResponseEntity<>(ListCommonRes.toResponse(invoices), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
