package com.example.houserental.exception.facility;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityException extends RuntimeException{
    private String message;
}
