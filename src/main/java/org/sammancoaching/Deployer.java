package org.sammancoaching;

import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Responsável pelo deployment do projeto.
 */
public class Deployer {
    private final Logger logger;

    public Deployer(Logger logger) {
        this.logger = logger;
    }

    public boolean deploy(Project project, boolean testsPassed) {
        if (!testsPassed) {
            return false;
        }

        return deployProject(project);
    }

    private boolean deployProject(Project project) {
        PipelineResult deployResult = PipelineResult.fromString(project.deploy());
        
        if (deployResult.isSuccess()) {
            logger.info("Deployment successful");
            return true;
        } else {
            logger.error("Deployment failed");
            return false;
        }
    }
}
