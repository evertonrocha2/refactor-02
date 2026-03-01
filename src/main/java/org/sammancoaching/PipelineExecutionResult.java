package org.sammancoaching;

/**
 * Encapsula os resultados de uma execução do pipeline.
 * Fornece métodos para consultar o resultado e gerar notificações apropriadas.
 */
public class PipelineExecutionResult {
    private final boolean testsPassed;
    private final boolean deploySuccessful;

    /**
     * Cria um novo resultado de execução com os resultados fornecidos.
     *
     * @param testsPassed      se a execução dos testes foi bem-sucedida
     * @param deploySuccessful se o deployment foi bem-sucedido
     */
    public PipelineExecutionResult(boolean testsPassed, boolean deploySuccessful) {
        this.testsPassed = testsPassed;
        this.deploySuccessful = deploySuccessful;
    }

    /**
     * @return true se os testes passaram, false caso contrário
     */
    public boolean testsPassed() {
        return testsPassed;
    }

    /**
     * @return true se o deployment foi bem-sucedido, false caso contrário
     */
    public boolean deploySuccessful() {
        return deploySuccessful;
    }

    /**
     * Constrói uma mensagem de email apropriada baseada nos resultados da execução.
     * Prioridade: falha de teste > falha de deployment > sucesso
     *
     * @return uma mensagem descritiva resumindo a execução do pipeline
     */
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
