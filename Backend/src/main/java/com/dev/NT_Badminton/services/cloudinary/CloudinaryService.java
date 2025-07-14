package com.dev.NT_Badminton.services.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map<String, String> uploadFile(MultipartFile file, String folder) throws IOException;
    Map<String, String> updateFile(String publicId, MultipartFile newFile) throws IOException;
    String deleteFile(String publicId) throws IOException;
    public Map<String, Object> getFileDetails(String publicId) throws Exception;
}
