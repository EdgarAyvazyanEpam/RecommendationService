package com.recommendationservice.service.impl;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.exception.enumeration.FileExtension;
import com.recommendationservice.exception.exception.FileProcessingException;
import com.recommendationservice.exception.exception.InvalidFileFormatException;
import com.recommendationservice.exception.exception.InvalidUploadedFileException;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.CryptoImportService;
import com.recommendationservice.service.csvservice.CSVService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CryptoImportServiceImpl implements CryptoImportService {

  private final CSVService csvService;
  private final CryptoRepository cryptoRepository;
  private final ModelMapper modelMapper;
  private final UploadedFileRepository uploadedFileRepository;
  public static final String TYPE = "text/csv";

  @Override
  public List<CryptoEntity> cryptoProcess(MultipartFile file,
      UploadedFileEntity uploadedFileEntity) {
    return cryptoRepository.saveAll(
        csvService.readCryptoRateDtos(file, uploadedFileEntity).stream()
            .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class)).toList());
  }

  @Override
  public UploadedFileEntity processUploadedFile(
      MultipartFile file) {
    if (file != null) {
      validateFile(file);
      UploadedFileEntity entity = uploadedFileProcess(file);
      cryptoProcess(file, entity);
      return entity;
    }else {
        throw new FileProcessingException("File processing fail for:", "Uploaded file");
    }
  }

  @Override
  public void updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity) {

    if (!deleteCryptoValuesByFileId(uploadedFileEntity.getId())) {
      csvService.readCryptoRateDtos(file, uploadedFileEntity)
          .stream()
          .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class))
          .forEach(cryptoRepository::save);
    }
  }

    @Override
    public void updateStatus(UploadedFileEntity uploadedFile, UploadedFIleStatusEnum status) {
        if (uploadedFile != null) {
            uploadedFile.setFileStatus(status);
            uploadedFileRepository.save(uploadedFile);
        }
    }

    private boolean deleteCryptoValuesByFileId(Long id) {
    Optional<List<CryptoEntity>> cryptoEntities = cryptoRepository.deleteAllByUploadedFileEntityId(
        id);
    return cryptoEntities.isEmpty();
  }
  @Override
  public UploadedFileEntity uploadedFileProcess(MultipartFile file) {
    Optional<UploadedFileEntity> uploadedFileEntity = uploadedFileRepository
        .findFirstByFileNameAndFileStatus(file.getOriginalFilename(),
            UploadedFIleStatusEnum.STORED);
    if (uploadedFileEntity.isEmpty()) {
      return saveUploadedFile(file);
    } else {
      throw new FileProcessingException(
          String.format("The file already has been processed successfully in: %s",
              uploadedFileEntity.get().getCreationDate()), file.getOriginalFilename());
    }
  }

  @Override
  public UploadedFileEntity saveUploadedFile(MultipartFile file) {
    UploadedFileEntity entity = UploadedFileEntity.builder()
        .fileName(file.getOriginalFilename())
        .fileStatus(UploadedFIleStatusEnum.STORED)
        .creationDate(LocalDateTime.now())
        .build();
    return uploadedFileRepository.saveAndFlush(entity);
  }
  @Transactional
  @Override
  public void processUpdateUploadedFile(MultipartFile file) {
    if (file != null) {
      validateFile(file);
      UploadedFileEntity entity = updateUploadedFileDB(file);
      updateCrypto(file, entity);
    }
  }

    private void validateFile(MultipartFile file) {
        CryptoValueImportUtility.validateContent(file);
        CryptoValueImportUtility.validateExtension(file);
    }

    static class CryptoValueImportUtility {
        private CryptoValueImportUtility() {
        }

        private static void validateContent(MultipartFile file) {
            if (file == null || file.isEmpty()) {
                final String errorMessage = "The CSV file is empty or not provided. file:" + file;
                log.error(errorMessage);
                throw new InvalidUploadedFileException(errorMessage);
            }
        }

        private static void validateExtension(MultipartFile file) {
            if (!isFileExtensionCorrect(file, FileExtension.CSV.getExtension())) {
                log.error("Unsupported file type:");
                throw new InvalidFileFormatException("Wrong file type. Should be: " + FileExtension.CSV.getExtension());
            }
        }

        private static boolean isFileExtensionCorrect(MultipartFile file, String expectedExtension) {
            String fileName = file.getOriginalFilename();
            return fileName != null && expectedExtension.equalsIgnoreCase(getFileExtension(fileName));
        }

        private static String getFileExtension(String fileName) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
    }

  private UploadedFileEntity updateUploadedFileDB(MultipartFile file) throws FileProcessingException {
    Optional<UploadedFileEntity> uploadedFileEntity = uploadedFileRepository
        .findUploadedFileEntityByFileName(file.getOriginalFilename());
    if (uploadedFileEntity.isPresent()) {
      uploadedFileEntity.get().setFileStatus(UploadedFIleStatusEnum.UPDATED);
      return uploadedFileEntity.get();
    } else {
      throw new FileProcessingException(
              "Cannot fined corresponding file in DB by name: %s", file.getOriginalFilename());
    }
  }
}
