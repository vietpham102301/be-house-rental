package com.example.houserental.internal.models.room;

import com.example.houserental.controllers.models.RoomCreateRequest;
import com.example.houserental.internal.models.house.House;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house")
    private House house;

    private Integer floor;

    private Float area;

    private String status;

    @Column(name = "rent_fee")
    private Float rentFee;

    private String description;

    private int capacity;

    private int currentTenant;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;



}
