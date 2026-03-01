package org.sammancoaching;

import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Gerencia a fase de execução de testes do pipeline.
 * Responsável por executar os testes do projeto e registrar os resultados.
 */
public class TestRunner {
    private final Logger logger;

    /**
     * Cria um novo TestRunner com o logger especificado.
     *
     * @param logger serviço para registro de eventos de execução de testes
     */
    public TestRunner(Logger logger) {
        this.logger = logger;
    }

    /**
     * Executa os testes do projeto e registra o resultado.
     * Projetos sem testes são considerados como aprovados.
     *
     * @param project o projeto a ser testado
     * @return true se os testes passaram ou não existem testes, false se os testes falharam
     */
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
