package org.sammancoaching;

/**
 * Representa o resultado de uma operação do pipeline (teste ou deployment).
 * Substitui strings mágicas por valores enum type-safe.
 */
public enum PipelineResult {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;

    PipelineResult(String value) {
        this.value = value;
    }

    /**
     * Converte um resultado string para um enum PipelineResult.
     * Qualquer valor diferente de "success" é tratado como FAILURE.
     *
     * @param value o resultado string de uma operação
     * @return SUCCESS se o valor for "success", FAILURE caso contrário
     */
    public static PipelineResult fromString(String value) {
        return SUCCESS.value.equals(value) ? SUCCESS : FAILURE;
    }

    /**
     * @return true se este resultado representa sucesso
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }

    /**
     * @return true se este resultado representa falha
     */
    public boolean isFailure() {
        return this == FAILURE;
    }
}
