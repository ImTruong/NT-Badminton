package com.dev.NT_Badminton.services.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UploadFileService {

    UploadFile saveUploadFile(UploadFile uploadFile);

    Optional<UploadFile> getUserAvatar(int userAvatarId);

    boolean checkExistenceOfUploadFile(long uploadFileId);

    UploadFile uploadFile(MultipartFile file,String folder) throws Exception;

    UploadFile updateFile(MultipartFile file, UploadFile oldFile) throws Exception;

    void deleteFile(UploadFile uploadFile) throws Exception;

    Optional<UploadFile> getUploadFileById(long uploadFileId);

}
