package com.dev.NT_Badminton.repositories.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;

import java.util.Optional;

public interface UploadFileRepositoryCustom {
    Optional<UploadFile> getUserProfileImage(int userAvatarId);
}
