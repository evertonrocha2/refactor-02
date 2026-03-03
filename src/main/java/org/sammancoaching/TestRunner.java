package org.sammancoaching;

import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Executa testes do projeto e registra resultados.
 */
public class TestRunner {
    private final Logger logger;

    public TestRunner(Logger logger) {
        this.logger = logger;
    }

    public boolean executeTests(Project project) {
        if (!project.hasTests()) {
            logNoTests();
            return true;
        }

        return runTestsAndLog(project);
    }

    private void logNoTests() {
        logger.info("No tests");
    }

    private boolean runTestsAndLog(Project project) {
        PipelineResult testResult = PipelineResult.fromString(project.runTests());
        
        if (testResult.isSuccess()) {
            logger.info("Tests passed");
            return true;
        } else {
            logger.error("Tests failed");
            return false;
        }
    }
}
