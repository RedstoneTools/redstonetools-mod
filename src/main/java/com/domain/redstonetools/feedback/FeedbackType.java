package com.domain.redstonetools.feedback;

public enum FeedbackType {
    SUCCESS(1),
    NONE(0),
    WARNING(0),
    ERROR(-1);

    private final int code;

    FeedbackType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
