package org.sammancoaching;

/**
 * Resultado de operações do pipeline.
 */
public enum PipelineResult {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;

    PipelineResult(String value) {
        this.value = value;
    }

    public static PipelineResult fromString(String value) {
        return SUCCESS.value.equals(value) ? SUCCESS : FAILURE;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public boolean isFailure() {
        return this == FAILURE;
    }
}
