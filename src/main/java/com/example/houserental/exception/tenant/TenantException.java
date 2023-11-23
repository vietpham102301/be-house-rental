package com.example.houserental.exception.tenant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantException extends RuntimeException{
    private String message;
}
