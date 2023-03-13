package com.recommendationservice.repository;

import com.recommendationservice.entity.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoRepository extends JpaRepository<CryptoEntity, Long> {

    Optional<List<CryptoEntity>> deleteAllByUploadedFileEntityId(Long id);

    List<CryptoEntity> findCryptoValueBySymbol(String symbol);

    Optional<CryptoEntity> findFirstBySymbolOrderByPriceDateAsc(String symbol);

    Optional<CryptoEntity> findFirstBySymbolOrderByPriceDateDesc(String symbol);

    Optional<CryptoEntity> findFirstBySymbolOrderByPriceAsc(String symbol);

    Optional<CryptoEntity> findFirstBySymbolOrderByPriceDesc(String symbol);

}
