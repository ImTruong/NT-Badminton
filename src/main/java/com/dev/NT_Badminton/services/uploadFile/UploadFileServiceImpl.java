package com.dev.NT_Badminton.services.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Override
    public UploadFile insertFile(UploadFile uploadFile) {
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


}
