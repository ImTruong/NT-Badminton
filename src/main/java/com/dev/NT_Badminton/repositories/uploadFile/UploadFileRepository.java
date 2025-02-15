package com.dev.NT_Badminton.repositories.uploadFile;


import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    UploadFile findUploadFileById(int id);

    UploadFile findUploadFileByIdAndDeleted(int id, boolean deleted);

    List<UploadFile> findAllByIdInAndDeleted(List<Integer> ids, boolean deleted);

    boolean existsByIdAndDeleted(int id, boolean deleted);
}
