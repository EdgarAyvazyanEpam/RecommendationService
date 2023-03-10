package com.recommendationservice;

import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.service.CryptoImportService;
import com.recommendationservice.service.impl.CryptoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CryptoValueRecServiceEventListener {

    @Value("#{new Boolean('${event.listener.enabled}')}")
    private Boolean isEventListenerEnabled;

    @Value("${csv.file.storage.files}")
    private List<String> cryptoValueCsvFiles;

    private CryptoServiceImpl cryptoService;

    private CryptoImportService cryptoImportService;

    @Autowired
    public CryptoValueRecServiceEventListener(CryptoServiceImpl cryptoService, CryptoImportService cryptoImportService) {
        this.cryptoService = cryptoService;
        this.cryptoImportService = cryptoImportService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startApp() {
        if (isEventListenerEnabled()) {
            log.info("Event Listener is enabled");
            log.info("Starting to read Crypto Value csv file");
            log.info("Already usefully processed file (by name) will not be imported to avoid duplication");

            if (cryptoValueCsvFiles == null || cryptoValueCsvFiles.isEmpty()) {
                log.warn("There is no crypto value csv file to process");
            } else {
                startProcessCryptoValueCsvFiles();
            }
        } else {
            log.warn("Event Listener is not enabled");
        }
    }

    private boolean isEventListenerEnabled() {
        return isEventListenerEnabled != null && isEventListenerEnabled;
    }

    private void startProcessCryptoValueCsvFiles() {
        UploadedFileEntity uploadedFileEntity = null;
        for (String cryptoValueCsvFile : cryptoValueCsvFiles) {
            try {
                File file = ResourceUtils.getFile("classpath:" + cryptoValueCsvFile);
                uploadedFileEntity = cryptoImportService.processUploadedFile(convertFileToMultipartFile(file));
                log.warn("Crypto values are imported from {}", cryptoValueCsvFile);
            } catch (Exception ex) {
                cryptoImportService.updateStatus(uploadedFileEntity, UploadedFIleStatusEnum.PROCESSING_FAILED);
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
