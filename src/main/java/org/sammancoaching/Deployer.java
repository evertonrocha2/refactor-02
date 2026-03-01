package org.sammancoaching;

import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Gerencia a fase de deployment do pipeline.
 * Só realiza o deploy se os testes tiverem passado, registra os resultados do deployment.
 */
public class Deployer {
    private final Logger logger;

    /**
     * Cria um novo Deployer com o logger especificado.
     *
     * @param logger serviço para registro de eventos de deployment
     */
    public Deployer(Logger logger) {
        this.logger = logger;
    }

    /**
     * Faz o deploy do projeto para produção se os testes passaram.
     * O deployment é ignorado se os testes falharam.
     *
     * @param project     o projeto a ser deployado
     * @param testsPassed se a fase de testes foi bem-sucedida
     * @return true se o deployment foi bem-sucedido, false se os testes falharam ou o deployment falhou
     */
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
