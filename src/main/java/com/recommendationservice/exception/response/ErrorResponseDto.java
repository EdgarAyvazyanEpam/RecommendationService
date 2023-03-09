package com.recommendationservice.exception.response;

import com.recommendationservice.exception.enumeration.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private String code;

    private String title;

    private String message;

    private String timeStamp;

    private ErrorType type;
}