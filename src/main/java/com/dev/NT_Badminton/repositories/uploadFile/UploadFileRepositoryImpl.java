package com.dev.NT_Badminton.repositories.uploadFile;

import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.util.Optional;

public class UploadFileRepositoryImpl extends BaseRepository implements UploadFileRepositoryCustom {

    @Override
    public Optional<UploadFile> getUserProfileImage(int userAvatarId){
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        return Optional.ofNullable(query().selectFrom(qUploadFile)
                .where(qUploadFile.id.eq(userAvatarId))
                .fetchFirst());
    }


}
