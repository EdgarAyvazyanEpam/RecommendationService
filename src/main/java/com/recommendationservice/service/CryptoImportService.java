package com.recommendationservice.service;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CryptoImportService {
    List<CryptoEntity> cryptoProcess(MultipartFile file, UploadedFileEntity uploadedFileEntity);

    void updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity);

    UploadedFileEntity processUploadedFile(MultipartFile file);

    UploadedFileEntity initializeUploadedFile(MultipartFile file);

    void processUpdateUploadedFile(MultipartFile file);

    UploadedFileEntity uploadedFileProcess(MultipartFile file);

    void updateUploadedFileStatus(UploadedFileEntity uploadedFile, UploadedFIleStatusEnum status);
}
