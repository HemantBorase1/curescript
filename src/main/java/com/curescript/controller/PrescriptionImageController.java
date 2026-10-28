
package com.curescript.controller;

import com.curescript.service.CloudinaryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prescription-images")
public class PrescriptionImageController {

    private final CloudinaryService cloudinaryService;

    public PrescriptionImageController(
            CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String imageUrl =
                cloudinaryService.uploadPrescriptionImage(file);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Prescription image uploaded successfully");
        response.put("imageUrl", imageUrl);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
