package com.recommendationservice.service;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.domain.CryptoResponseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface CryptoService {
    List<CryptoRateDto> getCryptoValuesBySymbol(@NotBlank @NotNull String symbol);

    CryptoResponseDto getOldestCryptoValueBySymbol(@NotNull @NotBlank String symbol);

    CryptoResponseDto getNewestCryptoValueBySymbol(@NotNull @NotBlank String symbol);

    CryptoResponseDto getMinCryptoValueBySymbol(@NotNull @NotBlank String symbol);

    CryptoResponseDto getMaxCryptoValueBySymbol(@NotNull @NotBlank String symbol);

    List<CryptoResponseDto> getNormalizedCryptoBySymbolAndDate(String symbol, LocalDate date);

    List<CryptoResponseDto> getNormalizedCryptoBySymbol(String symbol);
}
