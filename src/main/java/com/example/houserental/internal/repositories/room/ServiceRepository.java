package com.example.houserental.internal.repositories.room;

import com.example.houserental.internal.models.room.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByRoom(int room);
}
