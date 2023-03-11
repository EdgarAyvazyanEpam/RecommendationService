package com.recommendationservice.service.impl;


import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.impl.utils.TestApiConstants;
import com.recommendationservice.service.impl.utils.TestValueConstants;
import org.junit.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CryptoValueControllerIntegrationTests extends MySqlDBIntegrationTestConfiguration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CryptoRepository cryptoValueRepository;
    @Autowired
    private UploadedFileRepository uploadedFileRepository;


    @Test
    public void testGetCryptoValuesBySymbolsShouldReturnOk() throws Exception {
        removeAndFill();
        String contentAsString = this.mockMvc
                .perform(get(TestApiConstants.API_CRYPTOS_CRYPTO, TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].symbol").value(TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andExpect(jsonPath("$.[0].price").isNotEmpty())
                .andExpect(jsonPath("$.[0].price").isNumber())
                .andExpect(jsonPath("$.[0].priceDate").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetCryptoValuesBySymbolsShouldReturnNotFound() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.API_CRYPTOS_CRYPTO, TestValueConstants.CRYPTO_VALUE_SYMBOL.substring(1, 2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetMinCryptoValuesBySymbolsShouldReturnOk() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.PATH_MIN + TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value(TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andExpect(jsonPath("$.price").isNotEmpty())
                .andExpect(jsonPath("$.price").isNumber())
                .andExpect(jsonPath("$.price").value(TestValueConstants.CRYPTO_VALUE_MIN))
                .andExpect(jsonPath("$.priceDate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetMinCryptoValuesBySymbolsShouldReturnNotFound() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.API_CRYPTOS_CRYPTO + TestApiConstants.PATH_MIN, TestValueConstants.CRYPTO_VALUE_SYMBOL.substring(1, 2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetMaxCryptoValuesBySymbolsShouldReturnOk() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.PATH_MAX + TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value(TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andExpect(jsonPath("$.price").isNotEmpty())
                .andExpect(jsonPath("$.price").isNumber())
                .andExpect(jsonPath("$.price").value(TestValueConstants.CRYPTO_VALUE_MAX))
                .andExpect(jsonPath("$.priceDate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testGetMaxCryptoValuesBySymbolsShouldReturnNotFound() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.API_CRYPTOS_CRYPTO + TestApiConstants.PATH_MAX, TestValueConstants.CRYPTO_VALUE_SYMBOL.substring(1, 2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetOldestCryptoValuesBySymbolsShouldReturnOk() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.PATH_OLDEST + TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value(TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andExpect(jsonPath("$.price").isNotEmpty())
                .andExpect(jsonPath("$.price").isNumber())
                .andExpect(jsonPath("$.price").value(TestValueConstants.CRYPTO_VALUE_OLDEST))
                .andExpect(jsonPath("$.priceDate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testGetOldestCryptoValuesBySymbolsShouldReturnNotFound() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.API_CRYPTOS_CRYPTO + TestApiConstants.PATH_OLDEST, TestValueConstants.CRYPTO_VALUE_SYMBOL.substring(1, 2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetNewestCryptoValuesBySymbolsShouldReturnOk() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.PATH_NEWEST + TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.symbol").value(TestValueConstants.CRYPTO_VALUE_SYMBOL))
                .andExpect(jsonPath("$.price").isNotEmpty())
                .andExpect(jsonPath("$.price").isNumber())
                .andExpect(jsonPath("$.price").value(TestValueConstants.CRYPTO_VALUE_NEWEST))
                .andExpect(jsonPath("$.priceDate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testGetNewestCryptoValuesBySymbolsShouldReturnNotFound() throws Exception {
        removeAndFill();
        this.mockMvc
                .perform(get(TestApiConstants.PATH_NEWEST + TestValueConstants.CRYPTO_VALUE_SYMBOL.substring(1, 2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private void removeAndFill() {
        cryptoValueRepository.deleteAll();
        List<CryptoEntity> cryptoValues = generateCryptoVales(TestValueConstants.CRYPTO_VALUE_SYMBOL);
        cryptoValueRepository.saveAllAndFlush(cryptoValues);
    }

    private List<CryptoEntity> generateCryptoVales(String symbol) {
        final List<CryptoEntity> cryptoValues = new ArrayList<>(4);

        long aDay = TimeUnit.DAYS.toMillis(1);
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        LocalDateTime hundredDaysAgo = LocalDateTime.ofInstant(Instant.ofEpochMilli(now - aDay * 100), TimeZone.getDefault().toZoneId());
        LocalDateTime tenDaysAgo = LocalDateTime.ofInstant(Instant.ofEpochMilli(now - aDay * 10), TimeZone.getDefault().toZoneId());
        LocalDateTime aDaysAgo = LocalDateTime.ofInstant(Instant.ofEpochMilli(now - aDay), TimeZone.getDefault().toZoneId());

        cryptoValues.add(generateCryptoVale(symbol, TestValueConstants.CRYPTO_VALUE_OLDEST, hundredDaysAgo));
        cryptoValues.add(generateCryptoVale(symbol, TestValueConstants.CRYPTO_VALUE_MIN, tenDaysAgo));
        cryptoValues.add(generateCryptoVale(symbol, TestValueConstants.CRYPTO_VALUE_MAX, tenDaysAgo));
        cryptoValues.add(generateCryptoVale(symbol, TestValueConstants.CRYPTO_VALUE_NEWEST, aDaysAgo));
        return cryptoValues;
    }

    private CryptoEntity generateCryptoVale(String symbol, BigDecimal price, LocalDateTime priceDate) {
        UploadedFileEntity uploadedFileEntity = uploadedFileRepository.saveAndFlush(new UploadedFileEntity(1L, "CSV File name",
                UploadedFIleStatusEnum.STORED, LocalDateTime.now(), new HashSet<>()));
        return CryptoEntity.builder()
                .priceDate(priceDate)
                .symbol(symbol)
                .price(price)
                .uploadedFileEntity(uploadedFileEntity)
                .build();
    }
}
