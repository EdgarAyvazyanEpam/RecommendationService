package com.recommendationservice.controller;

import com.recommendationservice.exception.response.CryptoValueCsvUploadResponse;
import com.recommendationservice.service.CryptoImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/file")
@AllArgsConstructor
@Slf4j
@Api(value = "API", tags = {"Recommendation API for Crypto values"})
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
public class CSVFileProcessController {

    private final CryptoImportService fileService;

    @PostMapping(value = "/upload-crypto-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(
            nickname = "Upload CSV file",
            value = "POST operation to upload CSV file",
            responseContainer = "ResponseEntity<ResponseMessage> object"
    )
    public ResponseEntity<CryptoValueCsvUploadResponse> uploadFile(
            @Parameter(description = "Files to be uploaded", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {

        fileService.processUploadedFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CryptoValueCsvUploadResponse("File uploaded by name: " + file.getOriginalFilename() + "!"));
    }

    @PutMapping(value = "/update-crypto-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(
            nickname = "Upload CSV file",
            value = "POST operation to upload CSV file",
            responseContainer = "ResponseEntity<ResponseMessage> object"
    )
    public ResponseEntity<CryptoValueCsvUploadResponse> updateUploadedFile(
            @Parameter(description = "Files to be uploaded", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {

        fileService.processUpdateUploadedFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CryptoValueCsvUploadResponse("File updated by name: " + file.getOriginalFilename() + "!"));
    }
}
