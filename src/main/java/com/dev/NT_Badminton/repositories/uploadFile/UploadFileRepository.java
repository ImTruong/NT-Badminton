package com.dev.NT_Badminton.repositories.uploadFile;


import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long>, UploadFileRepositoryCustom {
}
