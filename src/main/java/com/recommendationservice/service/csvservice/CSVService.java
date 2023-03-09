package com.recommendationservice.service.csvservice;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.entity.UploadedFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    List<CryptoRateDto> readCryptoRateDtos(MultipartFile file, UploadedFileEntity uploadedFileEntity);
}
