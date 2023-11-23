package com.example.houserental.services.image;

import com.example.houserental.internal.models.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<Image> getImagesByEntityId(int entityId, String imageType);
    boolean uploadImage(List<MultipartFile> file, int entityId, String imageType) throws IOException;
    byte[] getImage(int imageId) throws IOException;
    boolean deleteImage(int imageId);
}
