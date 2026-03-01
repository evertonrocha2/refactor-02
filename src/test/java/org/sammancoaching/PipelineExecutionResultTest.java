package org.sammancoaching;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PipelineExecutionResultTest {

    @Test
    void shouldReturnTestsFailedMessageWhenTestsFailed() {
        // Arrange
        PipelineExecutionResult result = new PipelineExecutionResult(false, false);

        // Act
        String message = result.buildEmailMessage();

        // Assert
        assertThat(message).isEqualTo("Tests failed");
    }

    @Test
    void shouldReturnDeploymentSuccessMessageWhenAllSucceeds() {
        // Arrange
        PipelineExecutionResult result = new PipelineExecutionResult(true, true);

        // Act
        String message = result.buildEmailMessage();

        // Assert
        assertThat(message).isEqualTo("Deployment completed successfully");
    }

    @Test
    void shouldReturnDeploymentFailedMessageWhenTestsPassedButDeploymentFailed() {
        // Arrange
        PipelineExecutionResult result = new PipelineExecutionResult(true, false);

        // Act
        String message = result.buildEmailMessage();

        // Assert
        assertThat(message).isEqualTo("Deployment failed");
    }

    @Test
    void shouldExposeTestsPassedStatus() {
        // Arrange
        PipelineExecutionResult result = new PipelineExecutionResult(true, false);

        // Act & Assert
        assertThat(result.testsPassed()).isTrue();
    }

    @Test
    void shouldExposeDeploySuccessfulStatus() {
        // Arrange
        PipelineExecutionResult result = new PipelineExecutionResult(false, true);

        // Act & Assert
        assertThat(result.deploySuccessful()).isTrue();
    }
}
