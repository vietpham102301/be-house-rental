package com.example.houserental.controllers.image;


import com.example.houserental.services.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<Object> uploadImage(@RequestParam("files") List<MultipartFile> multipartFile, @RequestParam("entityId") int entityId, @RequestParam("imageType") String imageType) throws IOException {
        if(imageService.uploadImage(multipartFile, entityId, imageType)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Image has been uploaded");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.internalServerError().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") int imageId) throws IOException {
        byte[] imageData=imageService.getImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable("id") int imageId){
        if(imageService.deleteImage(imageId)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Image has been deleted");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.internalServerError().build();
    }
}
