package com.recommendationservice.repository;

import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFileEntity, Long> {
    Optional<UploadedFileEntity> findFirstByFileNameAndFileStatus(String fileName, UploadedFIleStatusEnum status);
    Optional<UploadedFileEntity> findUploadedFileEntityById(Long id);
    Optional<UploadedFileEntity> findUploadedFileEntityByFileName(String fileName);
    @Modifying
    @Query("update UploadedFileEntity up set up.fileStatus=:status where up.id=:id")
    void updateUploadedFileById(@Param(value = "status") String status, @Param(value = "id") Long id);
}
