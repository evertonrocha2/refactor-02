package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.Project;

import static org.assertj.core.api.Assertions.assertThat;

class DeployerTest {

    private CapturingLogger logger;
    private Deployer deployer;

    @BeforeEach
    void setUp() {
        logger = new CapturingLogger();
        deployer = new Deployer(logger);
    }

    @Test
    void shouldReturnFalseAndNotLogWhenTestsFailedWithoutDeploying() {
        // Arrange
        Project project = Project.builder()
                .setDeploysSuccessfully(true)
                .build();
        boolean testsPassed = false;

        // Act
        boolean result = deployer.deploy(project, testsPassed);

        // Assert
        assertThat(result).isFalse();
        assertThat(logger.getLoggedLines()).isEmpty();
    }

    @Test
    void shouldReturnTrueAndLogSuccessWhenDeploymentSucceeds() {
        // Arrange
        Project project = Project.builder()
                .setDeploysSuccessfully(true)
                .build();
        boolean testsPassed = true;

        // Act
        boolean result = deployer.deploy(project, testsPassed);

        // Assert
        assertThat(result).isTrue();
        assertThat(logger.getLoggedLines()).containsExactly("INFO: Deployment successful");
    }

    @Test
    void shouldReturnFalseAndLogErrorWhenDeploymentFails() {
        // Arrange
        Project project = Project.builder()
                .setDeploysSuccessfully(false)
                .build();
        boolean testsPassed = true;

        // Act
        boolean result = deployer.deploy(project, testsPassed);

        // Assert
        assertThat(result).isFalse();
        assertThat(logger.getLoggedLines()).containsExactly("ERROR: Deployment failed");
    }
}
