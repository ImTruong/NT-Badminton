package com.dev.NT_Badminton.services.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.entities.upload_file.constant.UploadFileType;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import com.dev.NT_Badminton.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public UploadFile saveUploadFile(UploadFile uploadFile) {
        return uploadFileRepository.save(uploadFile);
    }

    @Override
    public Optional<UploadFile> getUserAvatar(int userAvatarId) {
        return uploadFileRepository.getUserProfileImage(userAvatarId);
    }

    @Override
    public boolean checkExistenceOfUploadFile(long uploadFileId) {
        return uploadFileRepository.existsById(uploadFileId);
    }

    @Override
    public UploadFile uploadFile(MultipartFile file, String folder) throws Exception {
        if (file != null && !file.isEmpty()) {
            List<String> validImageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
            if (!validImageTypes.contains(file.getContentType())) {
                throw new IllegalArgumentException("Please send a valid image file (png, jpg, jpeg)");
            }
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file, folder);

            Map details = cloudinaryService.getFileDetails(uploadResult.get("publicId"));

            UploadFile uploadFile =
                    UploadFile.builder()
                            .originUrl(uploadResult.get("url"))
                            .type(UploadFileType.IMAGE)
                            .width((Integer) details.get("width"))
                            .height((Integer) details.get("height"))
                            .size((Integer) details.get("bytes"))
                            .publicId( (String) details.get("publicId"))
                            .build();
            return uploadFileRepository.save(uploadFile);
        }
        return null;
    }

    @Override
    public UploadFile updateFile(MultipartFile file, UploadFile oldFile) throws Exception {
        Map<String,String> newImage = cloudinaryService.updateFile(oldFile.getPublicId(), file);
        Map details = cloudinaryService.getFileDetails(newImage.get("public_id"));
        oldFile.setOriginUrl(details.get("url").toString());
        oldFile.setWidth((Integer) details.get("width"));
        oldFile.setHeight((Integer) details.get("height"));
        oldFile.setSize((Integer) details.get("bytes"));
        uploadFileRepository.save(oldFile);
        return oldFile;
    }

    @Override
    public void deleteFile(UploadFile uploadFile) throws Exception {
        cloudinaryService.deleteFile(uploadFile.getPublicId());
        uploadFileRepository.delete(uploadFile);
    }

    @Override
    public Optional<UploadFile> getUploadFileById(long uploadFileId) {
        return uploadFileRepository.findById(uploadFileId);
    }


}
