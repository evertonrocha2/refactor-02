package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.Project;
import org.sammancoaching.dependencies.TestStatus;

import static org.assertj.core.api.Assertions.assertThat;

class TestRunnerTest {

    private CapturingLogger logger;
    private TestRunner testRunner;

    @BeforeEach
    void setUp() {
        logger = new CapturingLogger();
        testRunner = new TestRunner(logger);
    }

    @Test
    void shouldReturnTrueAndLogWhenNoTests() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .build();

        // Act
        boolean result = testRunner.executeTests(project);

        // Assert
        assertThat(result).isTrue();
        assertThat(logger.getLoggedLines()).containsExactly("INFO: No tests");
    }

    @Test
    void shouldReturnTrueAndLogSuccessWhenTestsPass() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .build();

        // Act
        boolean result = testRunner.executeTests(project);

        // Assert
        assertThat(result).isTrue();
        assertThat(logger.getLoggedLines()).containsExactly("INFO: Tests passed");
    }

    @Test
    void shouldReturnFalseAndLogErrorWhenTestsFail() {
        // Arrange
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .build();

        // Act
        boolean result = testRunner.executeTests(project);

        // Assert
        assertThat(result).isFalse();
        assertThat(logger.getLoggedLines()).containsExactly("ERROR: Tests failed");
    }
}
