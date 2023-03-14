package com.recommendationservice.service.util;

import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.entity.CryptoEntity;

import java.util.ArrayList;
import java.util.List;

public class CryptoHelperImpl {
    private CryptoHelperImpl() {
    }

    public static CryptoResponseDto cryptoValueToCryptoResponseDto(CryptoEntity entity) {
        return new CryptoResponseDto(entity.getPriceDate(), entity.getSymbol(), entity.getPrice(), entity.getUploadedFileEntity().getId());
    }

    public static List<CryptoResponseDto> cryptoValuesToCryptoResponseDto(List<CryptoEntity> list) {
        return new ArrayList<>(list.stream().map(CryptoHelperImpl::cryptoValueToCryptoResponseDto).toList());
    }
}
