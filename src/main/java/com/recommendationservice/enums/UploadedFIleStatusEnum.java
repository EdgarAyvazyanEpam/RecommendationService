package com.recommendationservice.enums;

import java.util.HashMap;
import java.util.Map;

public enum UploadedFIleStatusEnum {
    UPDATED(1, "ALREADY_PROCESSED"),
    STORED(2, "STORED"),
    PROCESSING(3, "PROCESSING"),
    PROCESSING_FAILED(4,"PROCESSING_FAILED");
    private static final Map<Integer, UploadedFIleStatusEnum> codeMap = new HashMap<>();
    static {
        for (UploadedFIleStatusEnum s : UploadedFIleStatusEnum.values()) {
            codeMap.put(s.getCode(), s);
        }
    }
    private final int code;
    private final String name;

    UploadedFIleStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UploadedFIleStatusEnum getById(int id) {
        return codeMap.get(id);
    }
}
