package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Orquestra o fluxo de trabalho do pipeline de integração contínua.
 * Coordena a execução de testes, deployment e notificações por email.
 */
public class Pipeline {
    private final TestRunner testRunner;
    private final Deployer deployer;
    private final EmailNotifier emailNotifier;

    /**
     * Cria um novo Pipeline com as dependências especificadas.
     *
     * @param config  configuração para o comportamento do pipeline
     * @param emailer serviço para envio de notificações por email
     * @param logger  serviço para registro de eventos do pipeline
     */
    public Pipeline(Config config, Emailer emailer, Logger logger) {
        this.testRunner = new TestRunner(logger);
        this.deployer = new Deployer(logger);
        this.emailNotifier = new EmailNotifier(config, emailer, logger);
    }

    /**
     * Executa o fluxo completo do pipeline para um projeto.
     * Etapas: execução de testes → deployment (se testes passarem) → notificação por email
     *
     * @param project o projeto a ser processado pelo pipeline
     */
    public void run(Project project) {
        boolean testsPassed = testRunner.executeTests(project);
        boolean deploySuccessful = deployer.deploy(project, testsPassed);
        
        PipelineExecutionResult executionResult = new PipelineExecutionResult(testsPassed, deploySuccessful);
        emailNotifier.notifyIfEnabled(executionResult);
    }
}
