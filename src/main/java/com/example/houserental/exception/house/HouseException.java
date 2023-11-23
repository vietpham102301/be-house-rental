package com.example.houserental.exception.house;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseException extends RuntimeException{
    private String message;
}
