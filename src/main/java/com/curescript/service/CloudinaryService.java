
package com.curescript.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadPrescriptionImage(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a prescription image"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }

        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "curescript/prescriptions",
                        "resource_type", "image"
                )
        );

        Object secureUrl = uploadResult.get("secure_url");

        if (secureUrl == null) {
            throw new IOException(
                    "Cloudinary did not return an image URL"
            );
        }

        return secureUrl.toString();
    }
}
