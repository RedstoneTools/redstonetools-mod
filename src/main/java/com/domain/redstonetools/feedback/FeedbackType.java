package com.domain.redstonetools.feedback;

/**
 * The feedback level/type.
 * Signals basic status of the issuer.
 */
public enum FeedbackType {

    /**
     * Operation completed with success.
     */
    SUCCESS(1),

    /**
     * Operation completed.
     */
    NONE(0),

    /**
     * A warning was generated but the operation
     * completed.
     */
    WARNING(0),

    /**
     * An error occurred in the operation.
     */
    ERROR(-1)

    ;

    private final int code;

    FeedbackType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
