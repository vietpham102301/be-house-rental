package com.example.houserental.controllers.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String content;

}
