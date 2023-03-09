package com.recommendationservice.repository;

import com.recommendationservice.entity.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    //TODO procedure
    @Query(value = "select *, (select 100 * ((crypto1.price - min(crypto2.price)) / (max(crypto2.price) - min(crypto2.price))) " +
            " from crypto_values crypto2) as normalized_crypto_value from crypto_values crypto1 where crypto1.symbol = :symbol " +
            " and date(crypto1.price_date) = :date order by normalized_crypto_value desc limit 1", nativeQuery = true)
    Optional<CryptoEntity> findNormalizedByCryptoValueAndDate(@Param("symbol") String symbol, @Param("date") LocalDate date);


    //TODO procedure
    @Query(value = "select *, (select 100 * ((crypto1.price - min(crypto2.price)) / (max(crypto2.price) - min(crypto2.price))) " +
            " from crypto_values crypto2) as normalized_crypto_value from crypto_values crypto1 where crypto1.symbol = :symbol " +
            " order by normalized_crypto_value\n", nativeQuery = true)
    List<CryptoEntity> findNormalizedByCryptoValue(@Param("symbol") String symbol);
}
