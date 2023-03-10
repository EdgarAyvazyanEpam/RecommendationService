package com.recommendationservice.exception.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileExtension {
    CSV("csv");
    private final String extension;

    public String getExtension() {
        return extension;
    }

}