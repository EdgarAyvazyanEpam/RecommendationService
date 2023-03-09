package com.recommendationservice.service.csvservice.impl;

import com.recommendationservice.csv.helper.CSVCryptoHelper;
import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.service.csvservice.CSVService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CSVServiceImpl implements CSVService {
    @Override
    public List<CryptoRateDto> readCryptoRateDtos(MultipartFile file, UploadedFileEntity uploadedFileEntity) {
        return CSVCryptoHelper.csvToCryptoDto(file, uploadedFileEntity);
    }
}
