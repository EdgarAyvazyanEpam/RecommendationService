package com.recommendationservice.controller;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.service.CryptoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController("/api")
@Api(value = "API", tags = {"Crypto value recommendation-service API"})
@ApiResponses({
        @ApiResponse(code = 200, message = "Ok. The request processed successfully"),
        @ApiResponse(code = 201, message = "Created. The resource created successfully"),
        @ApiResponse(code = 204, message = "No Content. The server has successfully fulfilled the request and there is no additional content to send in the response payload body."),
        @ApiResponse(code = 400, message = "Bad Request. The request could not be understood by the server due to malformed syntax"),
        @ApiResponse(code = 401, message = "Unauthorised. The request requires client authentication"),
        @ApiResponse(code = 403, message = "Forbidden. The request requires client authorisation to access resource"),
        @ApiResponse(code = 404, message = "Not Found. The server cannot find the requested resource"),
        @ApiResponse(code = 429, message = "Too Many Requests. The number of requests from this client is restricted to a specified quota"),
        @ApiResponse(code = 500, message = "Internal Server Error. The server encountered an unexpected condition which prevented it from fulfilling the request"),
        @ApiResponse(code = 503, message = "Service Unavailable. The server is not ready to handle the request. If specified please check the"),
        @ApiResponse(code = 504, message = "Gateway Timeout. The server, while acting as a gateway or proxy, did not get a response in time from the"),
})
public class CryptoRecommendationController {

    private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final CryptoService cryptoService;


    @GetMapping("/cryptos/{crypto}")
    @ApiOperation(
            nickname = "Fetch crypto value",
            value = "GET operation to fetch crypto value by symbol",
            responseContainer = "ResponseEntity<List<CryptoRateDto>> object"
    )
    public ResponseEntity<List<CryptoRateDto>> getCryptoValuesBySymbol(@Parameter(description = "The crypto value symbol")
                                                                       @NonNull @NotBlank @PathVariable("crypto") String symbol) {
        log.info("Request to get details for the crypto value: {}", symbol);
        return new ResponseEntity<>(cryptoService.getCryptoValuesBySymbol(symbol), HttpStatus.OK);
    }

    @GetMapping("/oldest/{crypto}")
    @ApiOperation(
            nickname = "Fetch the oldest crypto value",
            value = "GET operation to fetch the oldest crypto value by symbol",
            responseContainer = "ResponseEntity<CryptoValueResponseDto> object"
    )
    public ResponseEntity<CryptoResponseDto> getOldestCryptoValueBySymbol(@Parameter(description = "The crypto value symbol to query")
                                                                          @NonNull @NotBlank @PathVariable("crypto") String symbol) {
        log.info("Requested to return the oldest value for the crypto value: {}", symbol);
        return ResponseEntity.ok(cryptoService.getOldestCryptoValueBySymbol(symbol));
    }

    @GetMapping("/newest/{crypto}")
    @ApiOperation(
            nickname = "Fetch the newest crypto value",
            value = "GET operation to fetch the newest crypto value by symbol",
            responseContainer = "ResponseEntity<CryptoValueResponseDto> object"
    )
    public ResponseEntity<CryptoResponseDto> getNewestCryptoValueBySymbol(@Parameter(description = "The crypto value symbol to query")
                                                                          @NonNull @NotBlank @PathVariable("crypto") String symbol) {
        log.info(String.format("Requested to return the newest value for the crypto value: %s", symbol));
        return ResponseEntity.ok(cryptoService.getNewestCryptoValueBySymbol(symbol));
    }

    @GetMapping("/min/{crypto}")
    @ApiOperation(
            nickname = "Fetch the minimum crypto value",
            value = "GET operation to fetch the minimum crypto value by symbol",
            responseContainer = "ResponseEntity<CryptoValueResponseDto> object"
    )
    public ResponseEntity<CryptoResponseDto> getMinCryptoValueBySymbol(@Parameter(description = "The crypto value symbol to query")
                                                                       @NonNull @NotBlank @PathVariable("crypto") String symbol) {
        log.info("Requested to return the min value for the crypto value: {}", symbol);
        return ResponseEntity.ok(cryptoService.getMinCryptoValueBySymbol(symbol));
    }

    @GetMapping("/max/{crypto}")
    @ApiOperation(
            nickname = "Fetch the maximum crypto value",
            value = "GET operation to fetch the maximum crypto value by symbol",
            responseContainer = "ResponseEntity<CryptoValueResponseDto> object"
    )
    public ResponseEntity<CryptoResponseDto> getMaxCryptoValueBySymbol(@Parameter(description = "The crypto value symbol to query")
                                                                       @NonNull @NotBlank @PathVariable("crypto") String symbol) {
        log.info("Requested to return the min value for the crypto value: {}", symbol);
        return ResponseEntity.ok(cryptoService.getMaxCryptoValueBySymbol(symbol));
    }

    @GetMapping("/normalized-at-date/{crypto}")
    @ApiOperation(
            nickname = "Fetch the normalized crypto value",
            value = "GET operation to fetch the normalized crypto value by symbol and date by format:" + DATE_FORMAT_YYYY_MM_DD,
            responseContainer = "ResponseEntity<CryptoValueResponseDto> object"
    )
    public ResponseEntity<List<CryptoResponseDto>> getNormalizedByCryptoValueByDate(@Parameter(description = "The crypto value symbol to query")
                                                                                    @NonNull @NotBlank @PathVariable("crypto") String symbol,
                                                                                    @Parameter(description = "The crypto value date to query")
                                                                                    @RequestParam("date") @DateTimeFormat(pattern = DATE_FORMAT_YYYY_MM_DD) LocalDate date) {

        log.info("Requested to return the normalized value for the crypto value {} and date {}", symbol, date);
        return ResponseEntity.ok(cryptoService.getNormalizedCryptoBySymbolAndDate(symbol, date));
    }

    @GetMapping("/normalized/{crypto}")
    @ApiOperation(
            nickname = "Fetch the normalized crypto values",
            value = "GET operation to fetch the normalized crypto values by symbol",
            responseContainer = "ResponseEntity<List<CryptoValueResponseDto>> object"
    )
    public ResponseEntity<List<CryptoResponseDto>> getNormalizedByCryptoValue(@Parameter(description = "The crypto value symbol to query")
                                                                              @NonNull @NotBlank @PathVariable("crypto") String symbol) {

        log.info("Requested to return the normalized value for the crypto value {}", symbol);
        return ResponseEntity.ok(cryptoService.getNormalizedCryptoBySymbol(symbol));
    }
}
