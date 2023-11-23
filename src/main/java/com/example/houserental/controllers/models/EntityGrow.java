package com.example.houserental.controllers.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntityGrow {
    private String name;
    private double number;
    private double growPercent;
}
