package com.example.houserental.exception.house;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HouseAlreadyExistException extends RuntimeException{
    private String message;
}
