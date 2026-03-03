package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Orquestra a execução do pipeline de CI/CD.
 */
public class Pipeline {
    private final TestRunner testRunner;
    private final Deployer deployer;
    private final EmailNotifier emailNotifier;

    public Pipeline(Config config, Emailer emailer, Logger logger) {
        this.testRunner = new TestRunner(logger);
        this.deployer = new Deployer(logger);
        this.emailNotifier = new EmailNotifier(config, emailer, logger);
    }

    public void run(Project project) {
        boolean testsPassed = testRunner.executeTests(project);
        boolean deploySuccessful = deployer.deploy(project, testsPassed);
        
        PipelineExecutionResult executionResult = new PipelineExecutionResult(testsPassed, deploySuccessful);
        emailNotifier.notifyIfEnabled(executionResult);
    }
}
