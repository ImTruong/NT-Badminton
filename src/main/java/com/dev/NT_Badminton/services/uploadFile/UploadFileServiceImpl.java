package com.dev.NT_Badminton.services.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Override
    public UploadFile createUploadFile(UploadFile uploadFile) {
        return uploadFileRepository.save(uploadFile);
    }

}
