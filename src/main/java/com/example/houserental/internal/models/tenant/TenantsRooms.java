package com.example.houserental.internal.models.tenant;

import com.example.houserental.internal.models.room.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tenants_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantsRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room")
    private Room room;

    private String status;
}