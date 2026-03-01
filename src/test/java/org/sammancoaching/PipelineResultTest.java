package org.sammancoaching;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PipelineResultTest {

    @Test
    void shouldCreateSuccessFromSuccessString() {
        // Act
        PipelineResult result = PipelineResult.fromString("success");

        // Assert
        assertThat(result).isEqualTo(PipelineResult.SUCCESS);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    void shouldCreateFailureFromFailureString() {
        // Act
        PipelineResult result = PipelineResult.fromString("failure");

        // Assert
        assertThat(result).isEqualTo(PipelineResult.FAILURE);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void shouldCreateFailureFromAnyOtherString() {
        // Act
        PipelineResult result = PipelineResult.fromString("unknown");

        // Assert
        assertThat(result).isEqualTo(PipelineResult.FAILURE);
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void shouldCreateFailureFromNullString() {
        // Act
        PipelineResult result = PipelineResult.fromString(null);

        // Assert
        assertThat(result).isEqualTo(PipelineResult.FAILURE);
        assertThat(result.isFailure()).isTrue();
    }
}
