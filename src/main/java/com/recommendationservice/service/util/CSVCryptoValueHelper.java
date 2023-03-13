package com.recommendationservice.service.util;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.exception.exception.CSVParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Component
public class CSVCryptoValueHelper {

    private CSVCryptoValueHelper() {

    }

    /**
     * Reading .csv file and create list of them
     *
     * @param file
     * @return List<CryptoRateDto>
     */
    public static List<CryptoRateDto> csvToCryptoDto(MultipartFile file, UploadedFileEntity uploadedFileEntity) {

        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.RFC4180.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CryptoRateDto> cryptoDTOS = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.stream().toList();

            for (CSVRecord csvRecord : csvRecords) {
                CryptoRateDto cryptoRateDto = new CryptoRateDto(
                        convertTimestampToLocalDateTime(csvRecord.get("timestamp")),
                        csvRecord.get("symbol"), new BigDecimal(csvRecord.get("price")),uploadedFileEntity);

                cryptoDTOS.add(cryptoRateDto);
            }

            return cryptoDTOS;
        } catch (IOException e) {
            String message = "Fail to parse CSV file";
            log.error(message, e);
            throw new CSVParseException(message + " " +  file.getOriginalFilename());
        }
    }

    /**
     * Converting date from csv file by timestamp number format to LocalDateTime
     *
     * @param timestampDate
     * @return LocalDateTime
     */
    private static LocalDateTime convertTimestampToLocalDateTime(String timestampDate) {

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestampDate)),
                TimeZone.getDefault().toZoneId());
    }
}
