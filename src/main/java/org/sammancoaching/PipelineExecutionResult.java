package org.sammancoaching;

/**
 * Encapsula resultados da execução do pipeline.
 */
public class PipelineExecutionResult {
    private final boolean testsPassed;
    private final boolean deploySuccessful;

    public PipelineExecutionResult(boolean testsPassed, boolean deploySuccessful) {
        this.testsPassed = testsPassed;
        this.deploySuccessful = deploySuccessful;
    }

    public boolean testsPassed() {
        return testsPassed;
    }

    public boolean deploySuccessful() {
        return deploySuccessful;
    }

    public String buildEmailMessage() {
        if (!testsPassed) {
            return "Tests failed";
        }
        
        if (deploySuccessful) {
            return "Deployment completed successfully";
        }
        
        return "Deployment failed";
    }
}
