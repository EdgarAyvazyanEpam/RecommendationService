package com.recommendationservice.service.impl;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.exception.exception.CryptoValueNotFoundException;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.CryptoService;
import com.recommendationservice.service.util.CryptoHelperImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.math.BigDecimal.valueOf;

@Slf4j
@Validated
@AllArgsConstructor
@Service
public class CryptoValueServiceImpl implements CryptoService {
    private final UploadedFileRepository uploadedFileRepository;

    private final CryptoRepository cryptoRepository;
    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    @Override
    public List<CryptoRateDto> getCryptoValuesBySymbol(@NotBlank @NotNull String symbol) {
        log.debug("Calling CryptoService.getCryptoValuesBySymbol method");
        List<CryptoEntity> cryptoEntityBySymbol = entityManager.createQuery("""
                select pc
                from CryptoEntity pc
                join fetch pc.uploadedFileEntity p where pc.symbol=:symbol
                """, CryptoEntity.class).setParameter("symbol", symbol).getResultList();
        if (cryptoEntityBySymbol.isEmpty()) {
            throw new CryptoValueNotFoundException("The values could not be found by " + symbol);
        }
        log.info("Found the following count for the requested crypto value:" + cryptoEntityBySymbol.size());
        return cryptoEntityBySymbol
                .stream()
                .map(cryptoEntity -> modelMapper.map(cryptoEntity, CryptoRateDto.class))
                .toList();
    }

    @Override
    public CryptoResponseDto getOldestCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getOldestCryptoValueBySymbol method");

        final CryptoEntity oldestCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDateAsc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The oldest value could not be found for the crypto value " + symbol));

        log.info("Found the following the oldest value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(oldestCryptoValue);
    }

    @Override
    public CryptoResponseDto getNewestCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getNewestCryptoValueBySymbol method");

        final CryptoEntity newestCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDateDesc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The newest value could not be found for the crypto value " + symbol));

        log.info("Found the following the newest value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(newestCryptoValue);
    }

    @Override
    public CryptoResponseDto getMinCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getMinCryptoValueBySymbol method");

        final CryptoEntity minCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceAsc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The min value could not be found for the crypto value " + symbol));

        log.info("Found the following the min value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(minCryptoValue);
    }

    @Override
    public CryptoResponseDto getMaxCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getMaxCryptoValueBySymbol method");
        log.info(String.format("Requested to find the max value for the crypto value: %s", symbol));

        final CryptoEntity maxCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDesc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The max value could not be found for the crypto value " + symbol));

        log.info("Found the following the max value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(maxCryptoValue);
    }

    @Override
    public List<CryptoResponseDto> getNormalizedCryptoBySymbolAndDate(String symbol, LocalDate date) {

        log.debug("Calling CryptoValueService.getNormalizedByCryptoValueAndDate method");
        List<CryptoEntity> cryptoValueBySymbol = cryptoRepository.findCryptoValueBySymbol(symbol);
        Double max = cryptoValueBySymbol
                .stream()
                .filter(cryptoEntity -> date.equals(cryptoEntity.getPriceDate().toLocalDate()))
                .map(CryptoEntity::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .max()
                .getAsDouble();

        Double min = cryptoValueBySymbol
                .stream()
                .filter(cryptoEntity -> date.equals(cryptoEntity.getPriceDate().toLocalDate()))
                .map(CryptoEntity::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .min().getAsDouble();


        List<CryptoEntity> collect = new ArrayList<>();
        for (CryptoEntity cryptoEntity : cryptoValueBySymbol) {
            if (valueOf(normalization(cryptoEntity.getPrice().doubleValue(), max, min, cryptoEntity.getPriceDate())).doubleValue() > 0.0) {
                collect.add(cryptoEntity);
            }
        }

        if (collect.isEmpty()) {
            throw new CryptoValueNotFoundException("The normalized value could not be found for the crypto value for the date: " + date + " " + symbol);
        }

        List<CryptoResponseDto> cryptoResponseDtos = CryptoHelperImpl.cryptoValuesToCryptoResponseDto(collect);
        cryptoResponseDtos.sort(Comparator.comparing(CryptoResponseDto::getPrice)
                .thenComparing(CryptoResponseDto::getPriceDate));
        Collections.reverse(cryptoResponseDtos);

        return cryptoResponseDtos;
    }

    private Double normalization(Double value, Double max, Double min, LocalDateTime date) {
        Double result = (value - min)/(max - min);//(max-min)/min
        if (result.isNaN()) {
            log.error("Normalized crypto value not found by this date:" + date);
            throw new CryptoValueNotFoundException("Normalized crypto value not found by this date:" + date);
        }
        return result;
    }

    @Override
    public List<CryptoResponseDto> getNormalizedCryptoBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getNormalizedCryptoValue method");
        List<CryptoEntity> cryptoValueBySymbol = cryptoRepository.findCryptoValueBySymbol(symbol);
        Double max = cryptoValueBySymbol
                .stream()
                .map(CryptoEntity::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .max()
                .getAsDouble();

        Double min = cryptoValueBySymbol
                .stream()
                .map(CryptoEntity::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .min().getAsDouble();


        List<CryptoEntity> collect = new ArrayList<>();
        for (CryptoEntity cryptoEntity : cryptoValueBySymbol) {
            if (valueOf(normalization(cryptoEntity.getPrice().doubleValue(), max, min, cryptoEntity.getPriceDate())).doubleValue() > 0.0) {
                collect.add(cryptoEntity);
            }
        }

        if (collect.isEmpty()) {
            throw new CryptoValueNotFoundException("The normalized value could not be found for the crypto value for the date: " + " " + symbol);
        }

        List<CryptoResponseDto> cryptoResponseDtos = CryptoHelperImpl.cryptoValuesToCryptoResponseDto(collect);
        cryptoResponseDtos.sort(Comparator.comparing(CryptoResponseDto::getPrice)
                .thenComparing(CryptoResponseDto::getPriceDate));
        Collections.reverse(cryptoResponseDtos);

        return cryptoResponseDtos;
    }
}
