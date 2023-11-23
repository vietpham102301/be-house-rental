package com.example.houserental.internal.repositories.image;

import com.example.houserental.internal.models.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByEntityIdAndImageType(int entityId, String imageType);
}
