package com.example.houserental.services.image;

import com.example.houserental.Common.Const;
import com.example.houserental.Common.Message;
import com.example.houserental.exception.image.ImageUploadException;
import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.repositories.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final String FOLDER_PATH="G:\\house-rental-images\\";
    @Override
    public List<Image> getImagesByEntityId(int entityId, String imageType) {
        List<Image> images = imageRepository.findAllByEntityIdAndImageType(entityId, imageType);
        return images;
    }

    @Override
    public boolean uploadImage(List<MultipartFile> files, int entityId, String imageType) throws IOException {

        if (files == null || files.isEmpty()) {
            return false;
        }

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.toLowerCase().endsWith(".jpg") && !originalFilename.toLowerCase().endsWith(".png")) {
                return false;
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return false;
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = imageType + "_" + entityId + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = FOLDER_PATH + newFileName;
            Image image = imageRepository.save(Image.builder()
                    .entityId(entityId)
                    .url(null)
                    .path(filePath)
                    .imageType(imageType)
                    .build());

            String imageURL = Const.IMAGE_DEFAULT + image.getId();
            image.setUrl(imageURL);
            image = imageRepository.save(image);
            file.transferTo(new File(filePath));

            if (image == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public byte[] getImage(int imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(()-> new ImageUploadException(Message.IMAGE_NOT_FOUND));
        String filePath= image.getPath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    @Override
    public boolean deleteImage(int imageId){
        Image image = imageRepository.findById(imageId).orElseThrow(()-> new ImageUploadException(Message.IMAGE_NOT_FOUND));
        String filePath= image.getPath();
        File file = new File(filePath);
        if(file.delete()){
            imageRepository.delete(image);
            return true;
        }
        return false;
    }
}
