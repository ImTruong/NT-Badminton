package com.dev.NT_Badminton.services.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map<String, String> uploadFile(MultipartFile file, String folder) throws IOException;
    String updateFile(String publicId, MultipartFile newFile) throws IOException;
    String deleteImage(String publicId) throws IOException;
}
