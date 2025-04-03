package com.dev.NT_Badminton.services.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;

import java.util.Optional;

public interface UploadFileService {

    UploadFile insertFile(UploadFile uploadFile);

    Optional<UploadFile> getUserAvatar(int userAvatarId);

    boolean checkExistenceOfUploadFile(long uploadFileId);

}
