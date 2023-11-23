package com.example.houserental.controllers.helper;

import com.example.houserental.controllers.models.TenantInforResponse;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class ListCommonRes {
    public static <T> Map<String, Object> toResponse(Page<T> pages) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", pages.getContent());
        res.put("currentPage", pages.getNumber());
        res.put("totalItems", pages.getTotalElements());
        res.put("totalPages", pages.getTotalPages());
        return res;
    }
}
