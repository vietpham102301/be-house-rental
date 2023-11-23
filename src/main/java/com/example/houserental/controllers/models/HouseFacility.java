package com.example.houserental.controllers.models;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HouseFacility {
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9 ]{1,50}$", message = "facility name just contain 1-50 alphabetical characters!")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9 ]{1,50}$", message = "Unit just contain number 0-9, 1-50 alphabetical characters!")
    private String unit;
    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0")
    private Float price;

}
