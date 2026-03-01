package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PipelineTest {

    private Config config;
    private Emailer emailer;
    private CapturingLogger logger;
    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        config = mock(Config.class);
        emailer = mock(Emailer.class);
        logger = new CapturingLogger();
        pipeline = new Pipeline(config, emailer, logger);
    }

    @Test
    void shouldLogTestsPassedAndDeploySuccessfullyWhenAllSucceeds() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(false);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: Tests passed",
                        "INFO: Deployment successful",
                        "INFO: Email disabled"
                );
        verifyNoInteractions(emailer);
    }

    @Test
    void shouldLogTestsFailedAndNotDeployWhenTestsFail() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(false);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "ERROR: Tests failed",
                        "INFO: Email disabled"
                );
        verifyNoInteractions(emailer);
    }

    @Test
    void shouldDeployWhenNoTests() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(false);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: No tests",
                        "INFO: Deployment successful",
                        "INFO: Email disabled"
                );
        verifyNoInteractions(emailer);
    }

    @Test
    void shouldLogDeploymentFailedWhenDeploymentFails() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(false)
                .build();
        when(config.sendEmailSummary()).thenReturn(false);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: Tests passed",
                        "ERROR: Deployment failed",
                        "INFO: Email disabled"
                );
        verifyNoInteractions(emailer);
    }

    @Test
    void shouldSendEmailWithSuccessMessageWhenAllSucceedsAndEmailEnabled() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(true);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: Tests passed",
                        "INFO: Deployment successful",
                        "INFO: Sending email"
                );
        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void shouldSendEmailWithDeploymentFailedMessageWhenDeploymentFailsAndEmailEnabled() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(false)
                .build();
        when(config.sendEmailSummary()).thenReturn(true);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: Tests passed",
                        "ERROR: Deployment failed",
                        "INFO: Sending email"
                );
        verify(emailer).send("Deployment failed");
    }

    @Test
    void shouldSendEmailWithTestsFailedMessageWhenTestsFailAndEmailEnabled() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(true);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "ERROR: Tests failed",
                        "INFO: Sending email"
                );
        verify(emailer).send("Tests failed");
    }

    @Test
    void shouldSendSuccessEmailWhenNoTestsButDeploymentSucceedsAndEmailEnabled() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(true);

        // Act
        pipeline.run(project);

        // Assert
        assertThat(logger.getLoggedLines())
                .containsExactly(
                        "INFO: No tests",
                        "INFO: Deployment successful",
                        "INFO: Sending email"
                );
        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void shouldNotAttemptDeploymentWhenTestsFail() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();
        when(config.sendEmailSummary()).thenReturn(true);

        // Act
        pipeline.run(project);

        // Assert - verify deploy was never called on the project
        // This is implicit because we only check that deployment logs don't appear
        assertThat(logger.getLoggedLines())
                .doesNotContain("INFO: Deployment successful")
                .doesNotContain("ERROR: Deployment failed");
    }
}
