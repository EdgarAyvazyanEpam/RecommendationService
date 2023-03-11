package com.recommendationservice.service.util;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.service.csvservice.CSVService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CSVImportServiceImpl implements CSVService {
    @Override
    public List<CryptoRateDto> readCryptoRateDtos(MultipartFile file, UploadedFileEntity uploadedFileEntity) {
        return CSVCryptoValueHelper.csvToCryptoDto(file, uploadedFileEntity);
    }
}
