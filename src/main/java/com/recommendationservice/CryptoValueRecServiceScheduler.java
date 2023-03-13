package com.recommendationservice;

import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.exception.exception.InvalidUploadedFileException;
import com.recommendationservice.service.CryptoImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class CryptoValueRecServiceScheduler {

    @Value("#{new Boolean('${scheduler.enabled}')}")
    private Boolean isSchedulerEnabled;
    private CryptoImportService cryptoImportService;
    private Set<String> cryptoValueCsvFiles;

    @Autowired
    public CryptoValueRecServiceScheduler(CryptoImportService cryptoImportService) {
        this.cryptoImportService = cryptoImportService;
    }

    @Scheduled(fixedRate = 60*60*100)
    @Async
    public void startApp() {

            cryptoValueCsvFiles = listFilesUsingJavaIO();


        if (isSchedulerEnabled()) {
            log.info("Event Listener is enabled");
            log.info("Starting to read Crypto Value csv file");
            log.info("The file has been already processed successfully");

            if (cryptoValueCsvFiles == null || cryptoValueCsvFiles.isEmpty()) {
                log.warn("There is no crypto value csv file to process");
            } else {
                startProcessCryptoValueCsvFiles();
            }
        } else {
            log.warn("Event Listener is not enabled");
        }
    }

    public Set<String> listFilesUsingJavaIO() {
        try {
            return Stream.of(Objects.requireNonNull(ResourceUtils.getFile("classpath:" + "crypto-values/").listFiles()))
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .filter(name -> name.endsWith(".csv"))
                    .collect(Collectors.toSet());
        } catch (FileNotFoundException e) {
            log.error("Could not fined corresponding file in folder");
            throw new InvalidUploadedFileException(e);
        }
    }

    private boolean isSchedulerEnabled() {
        return isSchedulerEnabled != null && isSchedulerEnabled;
    }

    private void startProcessCryptoValueCsvFiles() {
        UploadedFileEntity uploadedFileEntity = null;
        for (String cryptoValueCsvFile : cryptoValueCsvFiles) {
            try {
                File file = ResourceUtils.getFile("classpath:" + "crypto-values/" + cryptoValueCsvFile);
                uploadedFileEntity = cryptoImportService.processUploadedFile(convertFileToMultipartFile(file));
                log.warn("Crypto values are imported from {}", cryptoValueCsvFile);
            } catch (Exception ex) {
                cryptoImportService.updateUploadedFileStatus(uploadedFileEntity, UploadedFIleStatusEnum.PROCESSING_FAILED);
                log.warn("Could not import crypto values from {}", cryptoValueCsvFile);
                log.error(ex.getMessage());
            }
        }
    }

    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(file.getName(),file.getName(),"text/plain",input);
    }
}
